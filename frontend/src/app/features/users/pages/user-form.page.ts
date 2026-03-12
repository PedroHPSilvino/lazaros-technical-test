import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NotificationService } from '../../../core/services/notification.service';

import { ProfileApiService } from '../../profiles/services/profile-api.service';
import { UserApiService } from '../services/user-api.service';
import { Profile } from '../../../shared/models/profile.model';

@Component({
  selector: 'app-user-form-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
  ],
  template: `
    <mat-card>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h1 style="margin: 0;">{{ isEditMode() ? 'Edit User' : 'New User' }}</h1>
        <a mat-button routerLink="/users">Back</a>
      </div>

      <form [formGroup]="form" (ngSubmit)="submit()" style="display: flex; flex-direction: column; gap: 16px;">
        <mat-form-field appearance="outline">
          <mat-label>Name</mat-label>
          <input matInput formControlName="name" />

          <mat-error *ngIf="form.controls.name.hasError('required')">
            Name is required
          </mat-error>

          <mat-error *ngIf="form.controls.name.hasError('minlength')">
            Name must have at least 10 characters
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Profiles</mat-label>
          <mat-select formControlName="profileIds" multiple>
            <mat-option *ngFor="let profile of profiles()" [value]="profile.id">
              {{ profile.description }}
            </mat-option>
          </mat-select>

          <mat-error *ngIf="form.controls.profileIds.hasError('required')">
            At least one profile must be selected
          </mat-error>
        </mat-form-field>

        <div *ngIf="errorMessage()" style="color: #b00020;">
          {{ errorMessage() }}
        </div>

        <div style="display: flex; gap: 12px;">
          <button mat-raised-button type="submit" [disabled]="form.invalid || loading()">
            {{ loading() ? 'Saving...' : 'Save' }}
          </button>

          <a mat-button routerLink="/users">Cancel</a>
        </div>
      </form>
    </mat-card>
  `,
})
export class UserFormPage implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly userApiService = inject(UserApiService);
  private readonly profileApiService = inject(ProfileApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly notificationService = inject(NotificationService);

  protected readonly loading = signal(false);
  protected readonly isEditMode = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly profiles = signal<Profile[]>([]);

  private userId: number | null = null;

  protected readonly form = this.formBuilder.group({
    name: this.formBuilder.nonNullable.control('', [
      Validators.required,
      Validators.minLength(10),
    ]),
    profileIds: this.formBuilder.nonNullable.control<number[]>([], [
      Validators.required,
    ]),
  });

  ngOnInit(): void {
    this.loadProfiles();

    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.userId = Number(idParam);
      this.isEditMode.set(true);
      this.loadUser(this.userId);
    }
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      name: this.form.controls.name.value ?? '',
      profileIds: this.form.controls.profileIds.value ?? [],
    };

    this.loading.set(true);
    this.errorMessage.set('');

    const request$ =
      this.isEditMode() && this.userId !== null
        ? this.userApiService.update(this.userId, payload)
        : this.userApiService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.loading.set(false);
          this.notificationService.showSuccess(
            this.isEditMode() ? 'User updated successfully' : 'User created successfully'
          );
          void this.router.navigate(['/users']);
        },
        error: (error) => {
          this.loading.set(false);
          const message = error?.error?.message ?? 'Failed to save user';
          this.errorMessage.set(message);
          this.notificationService.showError(message);
          console.error('Failed to save user', error);
        },
      });
  }

  private loadProfiles(): void {
    this.profileApiService
      .findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (profiles) => {
          this.profiles.set(profiles);
        },
        error: (error) => {
          const message = error?.error?.message ?? 'Failed to load profiles';
          this.errorMessage.set(message);
          this.notificationService.showError(message);
          console.error('Failed to load profiles', error);
        },
      });
  }

  private loadUser(userId: number): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.userApiService
      .findById(userId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (user) => {
          this.form.patchValue({
            name: user.name,
            profileIds: user.profiles.map((profile) => profile.id),
          });
          this.loading.set(false);
        },
        error: (error) => {
          this.loading.set(false);
          this.errorMessage.set(error?.error?.message ?? 'Failed to load user');
          console.error('Failed to load user', error);
        },
      });
  }
}
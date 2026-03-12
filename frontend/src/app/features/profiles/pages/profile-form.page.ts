import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { ProfileApiService } from '../services/profile-api.service';

@Component({
  selector: 'app-profile-form-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  template: `
    <mat-card>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h1 style="margin: 0;">{{ isEditMode() ? 'Edit Profile' : 'New Profile' }}</h1>
        <a mat-button routerLink="/profiles">Back</a>
      </div>

      <form [formGroup]="form" (ngSubmit)="submit()" style="display: flex; flex-direction: column; gap: 16px;">
        <mat-form-field appearance="outline">
          <mat-label>Description</mat-label>
          <input matInput formControlName="description" />

          <mat-error *ngIf="form.controls.description.hasError('required')">
            Description is required
          </mat-error>

          <mat-error *ngIf="form.controls.description.hasError('minlength')">
            Description must have at least 5 characters
          </mat-error>
        </mat-form-field>

        <div *ngIf="errorMessage()" style="color: #b00020;">
          {{ errorMessage() }}
        </div>

        <div style="display: flex; gap: 12px;">
          <button mat-raised-button type="submit" [disabled]="form.invalid || loading()">
            {{ loading() ? 'Saving...' : 'Save' }}
          </button>

          <a mat-button routerLink="/profiles">Cancel</a>
        </div>
      </form>
    </mat-card>
  `,
})
export class ProfileFormPage implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly profileApiService = inject(ProfileApiService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly loading = signal(false);
  protected readonly isEditMode = signal(false);
  protected readonly errorMessage = signal('');

  private profileId: number | null = null;

  protected readonly form = this.formBuilder.group({
    description: ['', [Validators.required, Validators.minLength(5)]],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.profileId = Number(idParam);
      this.isEditMode.set(true);
      this.loadProfile(this.profileId);
    }
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const description = this.form.controls.description.value ?? '';
    const payload = { description };

    this.loading.set(true);
    this.errorMessage.set('');

    const request$ = this.isEditMode() && this.profileId !== null
      ? this.profileApiService.update(this.profileId, payload)
      : this.profileApiService.create(payload);

    request$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.loading.set(false);
          void this.router.navigate(['/profiles']);
        },
        error: (error) => {
          this.loading.set(false);
          this.errorMessage.set(
            error?.error?.message ?? 'Failed to save profile'
          );
          console.error('Failed to save profile', error);
        },
      });
  }

  private loadProfile(profileId: number): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.profileApiService
      .findById(profileId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (profile) => {
          this.form.patchValue({
            description: profile.description,
          });
          this.loading.set(false);
        },
        error: (error) => {
          this.loading.set(false);
          this.errorMessage.set(
            error?.error?.message ?? 'Failed to load profile'
          );
          console.error('Failed to load profile', error);
        },
      });
  }
}
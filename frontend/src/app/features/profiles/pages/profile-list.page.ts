import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NotificationService } from '../../../core/services/notification.service';

import { ProfileApiService } from '../services/profile-api.service';
import { Profile } from '../../../shared/models/profile.model';

@Component({
  selector: 'app-profile-list-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatDialogModule,
    MatSnackBarModule,
  ],
  template: `
    <mat-card>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h1 style="margin: 0;">Profiles</h1>
        <a mat-raised-button routerLink="/profiles/new">New Profile</a>
      </div>

      <table mat-table [dataSource]="profiles()" style="width: 100%;">
        <ng-container matColumnDef="id">
          <th mat-header-cell *matHeaderCellDef>ID</th>
          <td mat-cell *matCellDef="let profile">{{ profile.id }}</td>
        </ng-container>

        <ng-container matColumnDef="description">
          <th mat-header-cell *matHeaderCellDef>Description</th>
          <td mat-cell *matCellDef="let profile">{{ profile.description }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef>Actions</th>
          <td mat-cell *matCellDef="let profile">
            <button mat-button type="button" (click)="editProfile(profile.id)">Edit</button>
            <button mat-button type="button" (click)="deleteProfile(profile)">Delete</button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </mat-card>
  `,
})
export class ProfileListPage implements OnInit {
  private readonly profileApiService = inject(ProfileApiService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly dialog = inject(MatDialog);
  private readonly notificationService = inject(NotificationService);

  protected readonly profiles = signal<Profile[]>([]);
  protected readonly displayedColumns = ['id', 'description', 'actions'];

  ngOnInit(): void {
    this.loadProfiles();
  }

  protected editProfile(profileId: number): void {
    void this.router.navigate(['/profiles', profileId, 'edit']);
  }

  protected deleteProfile(profile: Profile): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Delete Profile',
        message: `Are you sure you want to delete "${profile.description}"?`,
        confirmButtonText: 'Delete',
        cancelButtonText: 'Cancel',
      },
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }

        this.profileApiService
          .delete(profile.id)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe({
            next: () => {
              this.notificationService.showSuccess('Profile deleted successfully');
              this.loadProfiles();
            },
            error: (error) => {
              this.notificationService.showError(
                error?.error?.message ?? 'Failed to delete profile'
              );
              console.error('Failed to delete profile', error);
            },
          });
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
          this.notificationService.showError(
            error?.error?.message ?? 'Failed to load profiles'
          );
          console.error('Failed to load profiles', error);
        },
      });
  }
}
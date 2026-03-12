import { CommonModule } from '@angular/common';
import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog.component';

import { UserApiService } from '../services/user-api.service';
import { User } from '../../../shared/models/user.model';

@Component({
  selector: 'app-user-list-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatDialogModule,
  ],
  template: `
    <mat-card>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <h1 style="margin: 0;">Users</h1>
        <a mat-raised-button routerLink="/users/new">New User</a>
      </div>

      <table mat-table [dataSource]="users()" style="width: 100%;">
        <ng-container matColumnDef="id">
          <th mat-header-cell *matHeaderCellDef>ID</th>
          <td mat-cell *matCellDef="let user">{{ user.id }}</td>
        </ng-container>

        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef>Name</th>
          <td mat-cell *matCellDef="let user">{{ user.name }}</td>
        </ng-container>

        <ng-container matColumnDef="profiles">
          <th mat-header-cell *matHeaderCellDef>Profiles</th>
          <td mat-cell *matCellDef="let user">
            {{ user.profiles.map(profile => profile.description).join(', ') }}
          </td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef>Actions</th>
          <td mat-cell *matCellDef="let user">
            <button mat-button type="button" (click)="editUser(user.id)">Edit</button>
            <button mat-button type="button" (click)="deleteUser(user)">Delete</button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </mat-card>
  `,
})
export class UserListPage implements OnInit {
  private readonly userApiService = inject(UserApiService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly dialog = inject(MatDialog);

  protected readonly users = signal<User[]>([]);
  protected readonly displayedColumns = ['id', 'name', 'profiles', 'actions'];

  ngOnInit(): void {
    this.loadUsers();
  }

  protected editUser(userId: number): void {
    void this.router.navigate(['/users', userId, 'edit']);
  }

  protected deleteUser(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Delete User',
        message: `Are you sure you want to delete "${user.name}"?`,
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

        this.userApiService
          .delete(user.id)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe({
            next: () => this.loadUsers(),
            error: (error) => {
              console.error('Failed to delete user', error);
            },
          });
      });
  }

  private loadUsers(): void {
    this.userApiService
      .findAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (users) => {
          this.users.set(users);
        },
        error: (error) => {
          console.error('Failed to load users', error);
        },
      });
  }
}
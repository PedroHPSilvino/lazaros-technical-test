import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
  ],
  template: `
    <mat-toolbar color="primary" class="navbar">
      <span>User Management</span>

      <span class="spacer"></span>

      <a mat-button routerLink="/users" routerLinkActive="active">Users</a>
      <a mat-button routerLink="/profiles" routerLinkActive="active">Profiles</a>
    </mat-toolbar>

    <main>
      <router-outlet></router-outlet>
    </main>
  `,
})
export class AppComponent {}
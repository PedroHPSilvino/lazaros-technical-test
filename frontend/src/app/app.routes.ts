import { Routes } from '@angular/router';

import { UserListPage } from './features/users/pages/user-list.page';
import { UserFormPage } from './features/users/pages/user-form.page';
import { ProfileListPage } from './features/profiles/pages/profile-list.page';
import { ProfileFormPage } from './features/profiles/pages/profile-form.page';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'users' },

  { path: 'users', component: UserListPage },
  { path: 'users/new', component: UserFormPage },
  { path: 'users/:id/edit', component: UserFormPage },

  { path: 'profiles', component: ProfileListPage },
  { path: 'profiles/new', component: ProfileFormPage },
  { path: 'profiles/:id/edit', component: ProfileFormPage },

  { path: '**', redirectTo: 'users' },
];
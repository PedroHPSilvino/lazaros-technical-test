import { Profile } from './profile.model';

export interface User {
  id: number;
  name: string;
  profiles: Profile[];
}
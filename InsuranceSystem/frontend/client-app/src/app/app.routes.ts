import { Routes } from '@angular/router'; 
import { LoginComponent } from './pages/login/login.component';
import { VerifyComponent } from './pages/verify/verify.component'; 
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';
import { ClientComponent } from './client/client.component';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { authGuard } from './guards/auth.guard';
import { RegisterComponent } from './pages/register/register.component';
import { InsurancesComponent } from './client/insurances/insurances.component'; 

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify', component: VerifyComponent },

  {
    path: 'client',
    component: ClientComponent,
    canActivate: [authGuard(['ADMIN', 'EMPLOYEE', 'CLIENT'])],
    children: [
      { path: '', redirectTo: 'policies', pathMatch: 'full' },
      { path: 'policies', component: ClientDashboardComponent },
      { path: 'my-policies', component: InsurancesComponent }
    ]
  },

  { path: 'unauthorized', component: UnauthorizedComponent }
];

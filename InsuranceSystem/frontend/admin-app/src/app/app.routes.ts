import { Routes } from '@angular/router'; 
import { LoginComponent } from './pages/login/login.component';
import { VerifyComponent } from './pages/verify/verify.component'; 
import { VerifySsoComponent } from './verify-sso/verify-sso.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { AdminComponent } from './admin/admin.component';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { authGuard } from './guards/auth.guard';
import { InsurancesComponent } from './admin/insurances/insurances.component'; 

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'verify', component: VerifyComponent },
  { path: 'verify-sso', component: VerifySsoComponent },

  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [authGuard(['ADMIN', 'EMPLOYEE'])],
    children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', component: AdminDashboardComponent },
      { path: 'policies', component: InsurancesComponent },

    ]
  },

  { path: 'unauthorized', component: UnauthorizedComponent }
];

import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { VerifyComponent } from './pages/verify/verify.component'; 
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { EmployeeDashboardComponent } from './employee/employee-dashboard/employee-dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verify', component: VerifyComponent }, 
  { path: 'client', component: ClientDashboardComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: 'employee', component: EmployeeDashboardComponent }
];

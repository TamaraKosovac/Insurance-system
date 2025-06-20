import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { AdminSidebarComponent } from './admin-sidebar/admin-sidebar.component';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterModule, AdminSidebarComponent],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }
}

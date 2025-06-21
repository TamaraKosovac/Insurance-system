import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { ClientSidebarComponent } from './client-sidebar/client-sidebar.component';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterModule, ClientSidebarComponent],
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css']
})
export class ClientComponent {
  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }
}

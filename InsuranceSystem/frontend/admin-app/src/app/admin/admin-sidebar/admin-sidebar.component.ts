import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component'; 

@Component({
  selector: 'app-admin-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-sidebar.component.html',
  styleUrls: ['./admin-sidebar.component.css']
})
export class AdminSidebarComponent {
  constructor(private router: Router, private dialog: MatDialog) {}

  logout() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        localStorage.clear();
        this.router.navigate(['/login']);
      }
    });
  }

  isUsersActive(): boolean {
  return this.router.url === '/admin' || this.router.url.startsWith('/admin/users');
  }

  isPoliciesActive(): boolean {
    return this.router.url.startsWith('/admin/policies');
  }
}

import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  template: `
    <div class="dialog-container">
      <h2 mat-dialog-title class="dialog-title">Logout confirmation</h2>
      <mat-dialog-content class="dialog-content">
        Are you sure you want to log out?
      </mat-dialog-content>
      <mat-dialog-actions align="end" class="dialog-actions">
        <button mat-button mat-dialog-close class="dialog-btn">Cancel</button>
        <button mat-button [mat-dialog-close]="true" class="dialog-btn">Log out</button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-container {
      background-color: #03595D;
      color: #ffffff;
      border-radius: 8px;
      padding: 16px;
    }

    .dialog-title {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 12px;
      color: #ffffff;
    }

    .dialog-content {
      font-size: 16px;
      color: #ffffff;
    }

    .dialog-actions button {
      font-weight: 500;
    }

    .dialog-btn {
      background-color: #ffffff !important;
      color: #03595D !important;
    }

    .dialog-btn:hover {
      background-color: #e0e0e0;
    }
  `]
})
export class ConfirmDialogComponent {}

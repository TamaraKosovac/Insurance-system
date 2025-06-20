import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent {
  username = localStorage.getItem('username') || '';
  code = '';

  toastMessage = '';
  showToast = false;
  toastType: 'success' | 'error' = 'success';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const body = {
      username: this.username,
      code: this.code
    };

    this.http.post<{ token: string }>('https://localhost:8443/auth/verify', body).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.showToastMessage('Verification successful!', 'success');

        const decodedToken: any = jwtDecode(response.token);
        const role = decodedToken.role;

        setTimeout(() => {
          if (role === 'ADMIN' || role === 'EMPLOYEE') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/client']);
          }
        }, 1000);
      },
      error: () => {
        this.showToastMessage('Invalid or expired verification code.', 'error');
      }
    });
  }

  resendCode(event: Event) {
    event.preventDefault();

    const username = localStorage.getItem('username');
    if (!username) {
      this.showToastMessage('Username not found in local storage.', 'error');
      return;
    }

    this.http.post<string>(
      'https://localhost:8443/auth/resend',
      { username },
      { responseType: 'text' as 'json' }
    ).subscribe({
      next: (res) => {
        this.showToastMessage(res, 'success');
      },
      error: () => {
        this.showToastMessage('Failed to resend the code.', 'error');
      }
    });
  }

  showToastMessage(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 3000);
  }
}

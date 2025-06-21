import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  showPassword: boolean = false;

  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
  this.http.get<string>(`https://localhost:8443/auth/role/${this.username}`, { responseType: 'text' as 'json' }).subscribe({
    next: (role: string) => {
      if (role === 'CLIENT') {
        this.showToastMessage('Impossible to log in.', 'error');
        return;
      }

      const loginData = {
        username: this.username,
        password: this.password
      };

      this.http.post('https://localhost:8443/auth/login', loginData).subscribe({
        next: () => {
          localStorage.setItem('username', this.username);
          this.showToastMessage('Login successful. Check your email for the verification code.', 'success');
          setTimeout(() => this.router.navigate(['/verify']), 1000);
        },
        error: () => {
          this.showToastMessage('Login failed. Please check your credentials.', 'error');
        }
      });
    },
    error: () => {
      this.showToastMessage('Could not fetch role. Please try again.', 'error');
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

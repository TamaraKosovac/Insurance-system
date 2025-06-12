import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './verify.component.html',
})
export class VerifyComponent {
  username = '';
  code = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const body = {
      username: this.username,
      code: this.code
    };

    this.http.post<{ token: string }>('http://localhost:8080/auth/verify', body).subscribe({
      next: (response) => {
        localStorage.setItem('jwtToken', response.token);
        alert('Verification successful!');

        // Sad ovdje možeš provjeriti ulogu korisnika ako backend vraća
        // ili ručno preusmjeriti:
        this.router.navigate(['/client']);
      },
      error: () => {
        alert('Invalid or expired verification code.');
      }
    });
  }
}

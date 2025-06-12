import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstname = '';
  lastname = '';
  username = '';
  email = '';
  password = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const formData = {
      firstname: this.firstname,
      lastname: this.lastname,
      username: this.username,
      email: this.email,
      password: this.password
    };

    this.http.post('http://localhost:8080/auth/register', formData).subscribe({
      next: () => {
        alert('Registration successful! You can now log in.');
        this.router.navigate(['/login']);
      },
      error: () => {
        alert('Registration failed. Please try again.');
      }
    });
  }
}

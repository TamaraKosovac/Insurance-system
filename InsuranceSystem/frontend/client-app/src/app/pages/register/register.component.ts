import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstname = '';
  lastname = '';
  username = '';
  email = '';
  password = '';
  showPassword = false;
  errorMessage = '';

  selectedFile: File | null = null;
  selectedFileName: string = '';

  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const strongPasswordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    if (!strongPasswordRegex.test(this.password)) {
      this.showToastMessage('Password must be at least 8 characters long and contain one uppercase letter, one number, and one special character.', 'error');
      return;
    }

    const formData = new FormData();
    formData.append('firstname', this.firstname);
    formData.append('lastname', this.lastname);
    formData.append('username', this.username);
    formData.append('email', this.email);
    formData.append('password', this.password);

    if (this.selectedFile) {
      formData.append('profileImage', this.selectedFile);
    }

    this.http.post('https://localhost:8443/auth/register', formData, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.showToastMessage(response, 'success');
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: (error) => {
        console.error(error);
        this.showToastMessage(error.error || 'Registration failed. Please try again.', 'error');
      }
    });
  }

  showToastMessage(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 3000);
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.selectedFileName = file.name;
    }
  }

  triggerFileInput(): void {
    const input = document.getElementById('profileImage') as HTMLElement;
    input?.click();
  }

  onFileInputKey(event: KeyboardEvent): void {
    const keysToClear = ['Delete', 'Backspace'];
    if (keysToClear.includes(event.key)) {
      this.clearSelectedFile();
      event.preventDefault();
    }
  }

  clearSelectedFile(): void {
    const input = document.getElementById('profileImage') as HTMLInputElement;
    if (input) {
      input.value = '';
    }
    this.selectedFile = null;
    this.selectedFileName = '';
  }
}

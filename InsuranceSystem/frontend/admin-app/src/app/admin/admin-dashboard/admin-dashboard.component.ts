import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';

interface UserDto {
  id: number;
  username: string;
  email: string;
  role: string;
  enabled: boolean;
  firstname: string;
  lastname: string;
  profileImageUrl: string;
  password?: string; 
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  users: UserDto[] = [];
  searchTerm: string = '';
  showAddForm: boolean = false;
  selectedUserId: number | null = null;
  role: string | null = null;

  newUser: Partial<UserDto> = {
    firstname: '',
    lastname: '',
    username: '',
    email: '',
    role: 'CLIENT'
  };
  password: string = '';

  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.setUserRole();
    this.loadUsers();
  }

  setUserRole(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const decoded: any = jwtDecode(token);
      this.role = decoded.role;
    }
  }

  isAdmin(): boolean {
    return this.role === 'ADMIN';
  }

  loadUsers(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<UserDto[]>('https://localhost:8443/api/users', { headers }).subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (err) => {
        console.error('Neuspješan zahtjev za korisnike:', err);
      }
    });
  }

  filteredUsers(): UserDto[] {
    return this.users.filter(user =>
      user.username.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  toggleOptions(userId: number) {
    this.selectedUserId = this.selectedUserId === userId ? null : userId;
  }

  isPasswordValid(password: string): boolean {
    const strongPasswordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
    return strongPasswordRegex.test(password);
  }

  saveUser(): void {
    if (!this.isAdmin()) return;

    if (!this.newUser.firstname || !this.newUser.lastname || !this.newUser.username ||
        !this.newUser.email || !this.newUser.role) {
      this.showToastMessage('All fields except password are required.', 'error');
      return;
    }

    if (!this.newUser.id && !this.password) {
      this.showToastMessage('Password is required for new users.', 'error');
      return;
    }

    if (this.password && !this.isPasswordValid(this.password)) {
      this.showToastMessage('The password must have at least 8 characters, an uppercase letter, a number and a special character.', 'error');
      return;
    }

    const existingUser = this.users.find(user => user.username === this.newUser.username && user.id !== this.newUser.id);
    if (existingUser) {
      this.showToastMessage('Username already exists.', 'error');
      return;
    }

    if (this.newUser.id) {
      this.updateUser();
      return;
    }

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const payload = {
      ...this.newUser,
      password: this.password
    };

    this.http.post<UserDto>('https://localhost:8443/api/users', payload, { headers }).subscribe({
      next: (createdUser) => {
        this.users.push(createdUser);
        this.resetForm();
        this.showToastMessage('User saved successfully.', 'success');
      },
      error: (err) => {
        console.error('Greška pri dodavanju korisnika:', err);
        this.showToastMessage('Greška pri dodavanju korisnika.', 'error');
      }
    });
  }

  updateUser(): void {
    if (!this.isAdmin()) return;

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const payload = {
      ...this.newUser,
      password: this.password || null
    };

    this.http.put<UserDto>(`https://localhost:8443/api/users/${this.newUser.id}`, payload, { headers }).subscribe({
      next: (updatedUser) => {
        this.users = this.users.map(u => u.id === updatedUser.id ? updatedUser : u);
        this.resetForm();
        this.showToastMessage('User updated successfully.', 'success');
      },
      error: () => {
        this.showToastMessage('Greška pri ažuriranju korisnika.', 'error');
      }
    });
  }

  deleteUser(id: number): void {
    if (!this.isAdmin()) return;

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.delete(`https://localhost:8443/api/users/${id}`, { headers }).subscribe({
      next: () => {
        this.users = this.users.filter(u => u.id !== id);
        this.showToastMessage('Korisnik je obrisan.', 'success');
      },
      error: () => {
        this.showToastMessage('Greška pri brisanju korisnika.', 'error');
      }
    });
  }

  editUser(user: UserDto): void {
    if (!this.isAdmin()) return;

    this.newUser = { ...user };
    this.password = '';
    this.showAddForm = true;
  }

  resetForm(): void {
    this.newUser = { role: 'CLIENT' };
    this.password = '';
    this.showAddForm = false;
  }

  showToastMessage(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 3000);
  }
}

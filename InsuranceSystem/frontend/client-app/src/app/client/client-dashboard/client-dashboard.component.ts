import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';

interface Policy {
  id?: number;
  name: string;
  type: string;
  amount: number;
  description: string;
}

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent implements OnInit {
  policies: Policy[] = [];
  filteredPolicies: Policy[] = [];
  filterType: string = '';
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  showToast = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPolicies();
  }

  loadPolicies(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<Policy[]>('https://localhost:8443/api/policies', { headers }).subscribe({
      next: (policies) => {
        this.policies = policies;
        this.applyFilters();
      },
      error: (err) => {
        console.error('Error loading policies:', err);
        this.showToastMessage('Error loading policies.', 'error');
      }
    });
  }

  applyFilters(): void {
    this.filteredPolicies = this.policies.filter(policy =>
      this.filterType ? policy.type === this.filterType : true
    );
  }

  showToastMessage(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 3000);
  }

  getPolicyIcon(policyType: string): string {
    switch (policyType) {
      case 'LIFE': return 'fas fa-heart';
      case 'TRAVEL': return 'fas fa-plane';
      case 'PROPERTY': return 'fas fa-home';
      case 'VEHICLE': return 'fas fa-car';
      case 'HEALTH': return 'fas fa-stethoscope';
      case 'LIABILITY': return 'fas fa-balance-scale';
      case 'ACCIDENT': return 'fas fa-exclamation-circle';
      case 'HOME': return 'fas fa-house-user';
      case 'CHILD': return 'fas fa-child';
      case 'HOUSEHOLD': return 'fas fa-cogs';
      default: return 'fas fa-question-circle';
    }
  }
}

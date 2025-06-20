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
  selector: 'app-insurances',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './insurances.component.html',
  styleUrls: ['./insurances.component.css']
})
export class InsurancesComponent implements OnInit {
  policies: Policy[] = [];
  filteredPolicies: Policy[] = [];
  filterType: string = '';
  showAddForm: boolean = false;
  selectedPolicyId: number | null = null;
  role: string | null = null;

  newPolicy: Policy = {
    name: '',
    type: '',
    amount: 0,
    description: ''
  };

  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  showToast = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.setUserRole();
    this.loadPolicies();
  }

  setUserRole(): void {
  const token = localStorage.getItem('token');
  if (token) {
    const decoded: any = jwtDecode(token);
    console.log('Decoded JWT:', decoded);
    if (decoded?.role) {
      this.role = `ROLE_${decoded.role}`; 
    }
  }
  console.log('User role:', this.role);
}


  isAdminOrEmployee(): boolean {
    return this.role === 'ROLE_ADMIN' || this.role === 'ROLE_EMPLOYEE';
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

  savePolicy(): void {
  console.log('Saving policy:', this.newPolicy);

  if (!this.isAdminOrEmployee()) {
    console.error('User not authorized');
    return;
  }

  const token = localStorage.getItem('token');
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

  const endpoint = this.newPolicy.id
    ? `https://localhost:8443/api/policies/${this.newPolicy.id}`
    : 'https://localhost:8443/api/policies';

  const method = this.newPolicy.id
    ? this.http.put<Policy>(endpoint, this.newPolicy, { headers })
    : this.http.post<Policy>(endpoint, this.newPolicy, { headers });

  method.subscribe({
    next: (response) => {
      console.log('Policy saved:', response);
      this.showToastMessage('Policy successfully saved.', 'success');
      this.loadPolicies();
      this.resetForm();
    },
    error: (err) => {
      console.error('Error saving policy:', err);
      this.showToastMessage('Error saving policy.', 'error');
    }
  });
}

  deletePolicy(id: number): void {
    if (!this.isAdminOrEmployee()) return;

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.delete(`https://localhost:8443/api/policies/${id}`, { headers }).subscribe({
      next: () => {
        this.showToastMessage('Policy successfully deleted.', 'success');
        this.policies = this.policies.filter(p => p.id !== id);
        this.applyFilters();
      },
      error: () => this.showToastMessage('Error deleting policy.', 'error')
    });
  }

  editPolicy(policy: Policy): void {
    if (!this.isAdminOrEmployee()) return;
    this.newPolicy = { ...policy };
    this.showAddForm = true;
  }

  resetForm(): void {
    this.newPolicy = { name: '', type: '', amount: 0, description: '' };
    this.showAddForm = false;
    this.selectedPolicyId = null;
  }

  toggleOptions(id: number): void {
    this.selectedPolicyId = this.selectedPolicyId === id ? null : id;
  }

  showToastMessage(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    this.showToast = true;
    setTimeout(() => this.showToast = false, 3000);
  }

  getPolicyIcon(policyType: string): string {
  switch (policyType) {
    case 'LIFE':
      return 'fas fa-heart'; 
    case 'TRAVEL':
      return 'fas fa-plane'; 
    case 'PROPERTY':
      return 'fas fa-home'; 
    case 'VEHICLE':
      return 'fas fa-car'; 
    case 'HEALTH':
      return 'fas fa-stethoscope'; 
    case 'LIABILITY':
      return 'fas fa-balance-scale'; 
    case 'ACCIDENT':
      return 'fas fa-exclamation-circle'; 
    case 'HOME':
      return 'fas fa-house-user'; 
    case 'CHILD':
      return 'fas fa-child'; 
    case 'HOUSEHOLD':
      return 'fas fa-cogs'; 
    default:
      return 'fas fa-question-circle'; 
  }
}
}

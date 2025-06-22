import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

interface PurchasedPolicy {
  id: number;
  name: string;
  type: string;
  amount: number;
  description: string;
  purchaseDate: string; 
}

@Component({
  selector: 'app-insurances',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './insurances.component.html',
  styleUrls: ['./insurances.component.css']
})
export class InsurancesComponent implements OnInit {
  policies: PurchasedPolicy[] = [];
  filteredPolicies: PurchasedPolicy[] = [];
  filterType: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPurchasedPolicies();
  }

  loadPurchasedPolicies(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<PurchasedPolicy[]>('https://localhost:8443/api/policies/purchased', { headers }).subscribe({
      next: (policies) => {
        this.policies = policies;
        this.applyFilters();
      },
      error: (err) => {
        console.error('Error loading purchased policies:', err);
      }
    });
  }

  applyFilters(): void {
    this.filteredPolicies = this.policies.filter(policy =>
      this.filterType ? policy.type === this.filterType : true
    );
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

  getTotalAmount(): number {
    return this.filteredPolicies.reduce((sum, policy) => sum + policy.amount, 0);
  }
}

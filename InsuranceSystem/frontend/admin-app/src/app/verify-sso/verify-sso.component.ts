import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-verify-sso',
  standalone: true,
  template: `<p>Verifying SSO login...</p>`,
})
export class VerifySsoComponent implements OnInit {
  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (token) {
      localStorage.setItem('token', token);
      this.router.navigate(['/admin']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}

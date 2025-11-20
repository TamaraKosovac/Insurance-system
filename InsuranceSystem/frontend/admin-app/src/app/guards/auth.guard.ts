import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

export const authGuard = (allowedRoles: string[]): CanActivateFn => {
  return () => {
    const router = inject(Router);
    const token = localStorage.getItem('token'); 

    if (!token) {
      router.navigate(['/login']);
      return false;
    }

    try {
      const decoded: any = jwtDecode(token);
      const role = decoded.role;
      const exp = decoded.exp * 1000;

      if (Date.now() > exp) {
        localStorage.removeItem('token');
        router.navigate(['/login']);
        return false;
      }

      if (!allowedRoles.includes(role)) {
        router.navigate(['/unauthorized']);
        return false;
      }

      return true;
    } catch {
      router.navigate(['/login']);
      return false;
    }
  };
};
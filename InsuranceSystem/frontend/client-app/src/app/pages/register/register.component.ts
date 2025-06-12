import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstname = '';
  lastname = '';
  username = '';
  email = '';
  password = '';

  onSubmit() {
    const formData = {
      firstname: this.firstname,
      lastname: this.lastname,
      username: this.username,
      email: this.email,
      password: this.password
    };

    console.log('Registration data:', formData);
    // ovdje kasnije ide poziv na backend
  }
}

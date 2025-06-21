import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifySsoComponent } from './verify-sso.component';

describe('VerifySsoComponent', () => {
  let component: VerifySsoComponent;
  let fixture: ComponentFixture<VerifySsoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerifySsoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifySsoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

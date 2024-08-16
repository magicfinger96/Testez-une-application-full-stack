import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
  tick,
} from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { DatePipe } from '@angular/common';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: false,
      id: 1,
    },
    logOut: jest.fn().mockResolvedValue(undefined) 
  };

  const mockUserService = {
    getById: jest.fn(),
    delete: jest.fn().mockReturnValue(of(undefined))
  };

  const user: User = {
    id: 1,
    email: 'john.wick@live.fr',
    lastName: 'Wick',
    firstName: 'John',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        DatePipe,
      ],
    }).compileComponents();

    mockUserService.getById.mockReturnValueOnce(of(user));
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Me Integration Test Suites', () => {
    it('should render user informations', () => {
      mockUserService.getById.mockReturnValue(of(user));
      component.ngOnInit();
      fixture.detectChanges();

      const datePipe = TestBed.inject(DatePipe);

      let nameElement =
        fixture.nativeElement.querySelectorAll('p')[0].textContent;
      let emailElement =
        fixture.nativeElement.querySelectorAll('p')[1].textContent;
      let createdAtElement = fixture.nativeElement.querySelector(
        '.p2 > p:first-of-type'
      ).textContent;
      let updatedAtElement = fixture.nativeElement.querySelector(
        '.p2 > p:nth-of-type(2)'
      ).textContent;

      expect(nameElement).toBe(
        `Name: ${user.firstName} ${user.lastName.toUpperCase()}`
      );

      expect(emailElement).toBe(`Email: ${user.email}`);
      expect(createdAtElement).toContain(
        `Create at:  ${datePipe.transform(user.createdAt, 'longDate')}`
      );
      expect(updatedAtElement).toContain(
        `Last update:  ${datePipe.transform(user.updatedAt, 'longDate')}`
      );
    });

    it('should render admin informations for admin users', () => {
      const adminUser: User = { ...user, admin: true };
      mockUserService.getById.mockReturnValue(of(adminUser));
      component.ngOnInit();
      fixture.detectChanges();

      const adminElement = fixture.nativeElement.querySelector('p.my2');
      expect(adminElement).toBeTruthy();
      expect(adminElement.textContent).toContain('You are admin');
    });

    it('should render deletion section for non-admin users', () => {
      const deleteElement = fixture.nativeElement.querySelector(
        '.my2 > p:first-of-type'
      );
      expect(deleteElement).toBeTruthy();
      expect(deleteElement.textContent).toContain('Delete my account:');
    });

    it('should call back method then window.history.back when hitting back button', fakeAsync(() => {
      jest.spyOn(component, 'back');
      jest.spyOn(window.history, 'back');

      const button = fixture.debugElement.nativeElement.querySelector(
        'button[mat-icon-button]'
      );
      button.click();
      tick();
      expect(component.back).toHaveBeenCalled();
      expect(window.history.back).toHaveBeenCalled();

      flush();
    }));

    it('should call delete method then userService delete method when hitting delete button', fakeAsync(() => {
      jest.spyOn(component, 'delete');
      jest.spyOn(mockSessionService, 'logOut');

      const button = fixture.debugElement.nativeElement.querySelector(
        'button[mat-raised-button]'
      );
      button.click();
      tick();

      expect(component.delete).toHaveBeenCalled();
      expect(mockUserService.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
      expect(mockSessionService.logOut).toHaveBeenCalled();

      flush();
    }));

  });
});

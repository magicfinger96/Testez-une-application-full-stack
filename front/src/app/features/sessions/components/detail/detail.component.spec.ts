import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, flush, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const mockedApiSession: Session = {
    id: 1,
    name: 'A session name',
    description: 'A session description',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockedTeacher: Teacher = {
    id: 1,
    lastName: "Finn",
    firstName: "Jack",
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockedApiSessionService = {
    detail: jest.fn().mockReturnValue(of(mockedApiSession)),
    participate: jest.fn().mockReturnValue(of(undefined)),
    unParticipate: jest.fn().mockReturnValue(of(undefined))
  };

  const mockedTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockedTeacher))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockedApiSessionService },
        { provide: TeacherService, useValue: mockedTeacherService }
      ],
    }).compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Detail Test Suites', () => {

    it('should render delete button for admin users', () => {
      const buttons : Element[] = fixture.nativeElement.querySelectorAll('button');

      const deleteButton = Array.from(buttons).find((button) => {
        return button.innerHTML?.includes('Delete');
      });

      expect(deleteButton).toBeTruthy();
    });

    it('should not render delete button for non-admin users', () => {
      component.isAdmin = false;
      fixture.detectChanges();

      const buttons : Element[] = fixture.nativeElement.querySelectorAll('button');

      const deleteButton = Array.from(buttons).find((button) => {
        return button.innerHTML?.includes('Delete');
      });

      expect(deleteButton).toBeFalsy();
    });

    it('should render "Do not participate" button for participating non-admin users', () => {
      component.isAdmin = false;
      fixture.detectChanges();
      
      const buttons : Element[] = fixture.nativeElement.querySelectorAll('button');

      const doNotParticipate = Array.from(buttons).find((button) => {
        return button.innerHTML?.includes('Do not participate');
      });

      expect(doNotParticipate).toBeTruthy();
    });

    it('should render "Participate" button for non-participating non-admin users', () => {
      component.isAdmin = false;

      const mockedApiSession2: Session = { ...mockedApiSession, users: [2, 3] };
      mockedApiSessionService.detail.mockReturnValue(of(mockedApiSession2));

      component.ngOnInit();
      fixture.detectChanges();
      
      const buttons : Element[] = fixture.nativeElement.querySelectorAll('button');

      const participate = Array.from(buttons).find((button) => {
        return button.innerHTML?.includes('Participate');
      });

      expect(participate).toBeTruthy();
    });

    it('should call participate method when hitting participate button', fakeAsync(() => {
      component.isAdmin = false;

      const mockedApiSession2: Session = { ...mockedApiSession, users: [2, 3] };
      mockedApiSessionService.detail.mockReturnValue(of(mockedApiSession2));

      component.ngOnInit();
      fixture.detectChanges();

      jest.spyOn(component, 'participate');

      const buttons : HTMLElement[] = fixture.nativeElement.querySelectorAll('button');

      const button = Array.from(buttons).find((button) => {
        return button.innerHTML?.includes('Participate');
      });

      mockedApiSessionService.detail.mockReturnValue(of(mockedApiSession));

      button!.click();
      tick();

      expect(component.participate).toHaveBeenCalled();

      flush();
    }));

    it('should set isParticipate to true and set proper teacher when calling participate method', () => {
      component.isParticipate = false;
      component.teacher = undefined;

      component.participate();
      
      expect(component.isParticipate).toBeTruthy();
      expect(component.teacher).toBe(mockedTeacher);
    });

    it('should set isParticipate to false and set proper teacher when calling unParticipate method', () => {
      component.isParticipate = true;
      component.teacher = undefined;

      const mockedApiSession2: Session = { ...mockedApiSession, users: [2, 3] };
      mockedApiSessionService.detail.mockReturnValue(of(mockedApiSession2));

      component.unParticipate();
      
      expect(component.isParticipate).toBeFalsy();
      expect(component.teacher).toBe(mockedTeacher);
    });
  
  });
});

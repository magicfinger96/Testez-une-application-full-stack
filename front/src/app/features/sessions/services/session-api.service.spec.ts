import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockedSessions: Session[] = [
    {
      id: 1,
      name: 'Session name',
      description: 'Session description',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2, 3],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      name: 'Session name 2',
      description: 'Session description 2',
      date: new Date(),
      teacher_id: 2,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Session API Test Suites', () => {
    it('should retrieve all sessions via GET', (done) => {
      service.all().subscribe({
        next: (sessions: Session[]) => {
          expect(sessions).toEqual(mockedSessions);
          done();
        },
        error: (err: any) => {
          fail(`Unexpected error: ${err}`);
        },
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush(mockedSessions);
    });

    it('should retrieve a session via GET', (done) => {
      service.detail('1').subscribe({
        next: (session: Session) => {
          expect(session).toEqual(mockedSessions[0]);
          done();
        },
        error: (err: any) => {
          fail(`Unexpected error: ${err}`);
        },
      });

      const req = httpMock.expectOne('api/session/1');
      expect(req.request.method).toBe('GET');
      req.flush(mockedSessions[0]);
    });

    it('should delete a session via DELETE', (done) => {
      service.delete('1').subscribe({
        next: (session: Session) => {
          expect(session).toEqual(mockedSessions[0]);
          done();
        },
        error: (err: any) => {
          fail(`Unexpected error: ${err}`);
        },
      });

      const req = httpMock.expectOne('api/session/1');
      expect(req.request.method).toBe('DELETE');
      req.flush(mockedSessions[0]);
    });

    it('should create a session via POST', (done) => {
      service.create(mockedSessions[0]).subscribe({
        next: (session: Session) => {
          expect(session).toEqual(mockedSessions[0]);
          done();
        },
        error: (err: any) => {
          fail(`Unexpected error: ${err}`);
        },
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockedSessions[0]);
      req.flush(mockedSessions[0]);
    });

    it('should update a session via POST', (done) => {
      service.update('1', mockedSessions[0]).subscribe({
        next: (session: Session) => {
          expect(session).toEqual(mockedSessions[0]);
          done();
        },
        error: (err: any) => {
          fail(`Unexpected error: ${err}`);
        },
      });

      const req = httpMock.expectOne('api/session/1');
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(mockedSessions[0]);
      req.flush(mockedSessions[0]);
    });

    it('should participate to a session via POST', () => {
      service.participate('1', '1').subscribe();
      const req = httpMock.expectOne('api/session/1/participate/1');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush(null);
    });

    it('should unparticipate to a session via DELETE', () => {
      service.unParticipate('1', '1').subscribe();
      const req = httpMock.expectOne('api/session/1/participate/1');
      expect(req.request.method).toBe('DELETE');
      expect(req.request.body).toBeNull();
      req.flush(null);
    });
  });
});

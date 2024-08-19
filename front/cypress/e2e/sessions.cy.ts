import '../support/commands';

describe('Sessions spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      {
        fixture: 'sessions.json',
      }
    );

    cy.login('yoga@studio.com', 'test!1234');
  });

  it('should create a session successfully as an admin user', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.intercept('GET', '/api/teacher', {
      fixture: 'teachers.json',
    });

    cy.get('button[routerLink="create"]').should('be.visible');
    cy.get('button[routerLink="create"]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type("My session");
    cy.get('input[formControlName=date]').type("2024-08-19");
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('David').click();
    cy.get('textarea[formControlName=description]').type("The description of my session");


    cy.intercept('POST', '/api/session', {
    });

    cy.get('button[type="submit"]').click();
    cy.url().should('equal', Cypress.config('baseUrl') + 'sessions');
  });

  it('should edit a session successfully as an admin user', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.intercept('GET', '/api/teacher', {
      fixture: 'teachers.json',
    });

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    });

    cy.get('button').contains('Edit').should('be.visible');
    cy.get('button').contains('Edit').click();
    cy.url().should('include', '/update/1');

    cy.get('textarea[formControlName=description]').clear().type("The updated description");


    cy.intercept('PUT', '/api/session/1', {}).as('updateSessionCall');

    cy.get('button[type="submit"]').click();

    cy.wait('@updateSessionCall').then((interception) => {
      expect(interception.request.body).to.deep.equal({
        name: "Session 1",
        date: "2024-09-19",
        teacher_id: 1,
        description: "The updated description"
      });
    });

    cy.url().should('equal', Cypress.config('baseUrl') + 'sessions');

  });

  it('should be able to delete a session as an admin user', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.intercept('GET', '/api/teacher/1', {
      fixture: 'teacher.json',
    });

    cy.intercept('GET', '/api/session/1', {
      fixture: 'session.json',
    });

    cy.get('button').contains('Detail').should('be.visible');
    cy.get('button').contains('Detail').click();
    cy.url().should('include', '/sessions/detail/1');

    cy.intercept('DELETE', '/api/session/1', {}).as('deleteSessionCall');

    cy.get('button').contains('Delete').should('be.visible');
    cy.get('button').contains('Delete').click();

    cy.url().should('equal', Cypress.config('baseUrl') + 'sessions');

  });

});

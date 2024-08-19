import '../support/commands';

describe('Me spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    );

    cy.login('yoga@studio.com', 'test!1234');
  });

  it('should display user details page successfully', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.get('span[routerLink=me]').click();
    cy.url().should('include', '/me');

    cy.contains('Name: jane DOE').should('be.visible');
    cy.contains('Email: jane.doe@live.fr').should('be.visible');
    cy.contains('Create at: August 19, 2024').should('be.visible');
    cy.contains('Last update: August 19, 2024').should('be.visible');
  });

  it('should delete user when hitting delete button', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.get('span[routerLink=me]').click();

    cy.intercept('DELETE', '/api/user/1', {});

    cy.get('button[mat-raised-button]').click();

    cy.url().should('equal', Cypress.config('baseUrl'));
  });
});

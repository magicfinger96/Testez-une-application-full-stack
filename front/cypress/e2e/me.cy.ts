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

  it('Display user details page successfully', () => {
    cy.intercept('GET', '/api/user/1', {
      fixture: 'user.json',
    });

    cy.get('span[routerLink=me]').click();
    cy.url().should('include', '/me');
  });
});

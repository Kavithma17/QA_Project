Feature: User Authentication
  As a user
  I want to sign up and log in
  So that I can access the system securely




  Scenario: Duplicate username signup
    Given a user already exists with username "john" and email "john@example.com" and password "pass123"
    When I try to signup with username "john" and email "john2@example.com" and password "pass456"
    Then the signup should fail with message "Username already taken"

  Scenario: Successful login
    Given a user already exists with username "alice" and email "alice@example.com" and password "mypassword"
    When I attempt to login with username "alice" and password "mypassword"
    Then the login should be successful

  Scenario: Failed login with wrong password
    Given a user already exists with username "bob" and email "bob@example.com" and password "secret"
    When I attempt to login with username "bob" and password "wrongpass"
    Then the login should fail

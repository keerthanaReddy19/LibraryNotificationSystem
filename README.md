# Library Book Due Notification System using Twilio API

This is a Java Spring Boot application for managing library transactions. The application sends SMS notifications to users when a book is issued or when a book is due, using Twilio's API. The core functionality revolves around issuing books, returning books, and sending reminders via SMS.

## Technologies Used
* Java 8
* Spring Boot
* Twilio API
* JUnit 5 for testing
* Mockito for unit testing
* PostgreSQL database
* Maven
  
## Project Features
1. Issue Book: A user can issue a book, and the system sends an SMS to confirm the issuance of the book, and provides the due date for the book to be returned.
2. Return Book: sends a confirmation SMS to return the book.
3. Due Date Reminder: A scheduled job to remind users of their books that are due to be returned.
4. Integration with Twilio: SMS notifications are handled using Twilio's Message.creator() API.

## Endpoints:
Issue a Book
This endpoint issues a book to a user and sends an SMS notification to the user with the due date.

URL: /api/transactions/issue
Method: POST

Parameters:
userId:Long
bookId:Long

Return a Book
This endpoint allows users to return a book and sends an SMS confirmation.

URL: /api/transactions/return
Method: POST

Parameters:
transactionId:Long

Get Due Transactions
This endpoint fetches a list of transactions that are due soon.

URL: /api/transactions/due
Method: GET

Get Overdue Transactions
This endpoint fetches a list of transactions that are overdue.

URL: /api/transactions/overdue
Method: GET 

## Architecture

1. Core Components:
   Controller: Manages the API endpoints.
   Service: Contains business logic. This includes handling book issuance, returns, and communication with Twilio's SMS API.  
   Repository: Uses JPA to communicate with the database.  

2. Twilio Integration:
   The application integrates with Twilio to send SMS notifications. In the TransactionService, the Twilio SDK is used to send messages using the following flow:
   Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), messageBody).create();

3. Error Handling:
   The application catches errors related to invalid transactions and returns appropriate error messages.
   For ex, if a book is not found, a custom exception, BookNotFoundException is thrown and handled at the controller level.

## End-to-End Flow:

1. Issuing a Book:
The user requests to issue a book via an API call.
The system checks if the book is available and whether the user exists.
If all checks pass, a new Transaction is created, and the book's availability is updated.
The system sends an SMS notification using Twilio to confirm the book's issuance.

2. Returning a Book:
The user initiates a return request.
The system retrieves the corresponding transaction, updates the book’s availability to available, and records the return date.
An SMS notification is sent to confirm the return.

## Unit Testing
Testing tools used:
JUnit 5 for running unit tests.  
Mockito for mocking dependencies, such as repositories and Twilio’s API.  

## Future Enhancements:
User Authentication: OAuth2/JWT-based authentication can be implemented for user login and securing the endpoints.  
Monitoring: Can be integrated with Splunk for better logging and monitoring of the requests.  
Cloud: The application can be deployed to a Cloud Service to maintain scalability and resilience.

## Database setup:
### Sql script:

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    phone_number VARCHAR(20) 
);

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    book_id BIGINT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);


-- Insert into users table
INSERT INTO users (name, phone_number) VALUES (‘Keerthana’, '+1234567890');
INSERT INTO users (name, phone_number) VALUES (‘Kathie’, '+1234567891');
INSERT INTO users (name, phone_number) VALUES (‘Keerthi’, '+1234567892');
INSERT INTO users (name, phone_number) VALUES (‘KR’, ‘+’1234567893');


-- Insert into books table
INSERT INTO books (title, author, available) VALUES ('The Book Thief’, ‘Markus Zusak’, true);
INSERT INTO books (title, author, available) VALUES ('1984', 'George Orwell', true);
INSERT INTO books (title, author, available) VALUES ('To Kill a Mockingbird', 'Harper Lee', true);
INSERT INTO books (title, author, available) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', true);

INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date) VALUES 
(1, 101, '2023-08-01', '2023-10-15', ’2023-10-14),
(2, 102, '2024-09-10', '2024-09-24', '2024-09-23'), 
(3, 103, '2024-09-20', '2024-10-04', NULL), 
(4, 104, '2024-09-25', '2024-10-09', '2024-10-08'); 

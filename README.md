# Oszczedzator3000 - Backend

# Table of contents 
* [About the project](#about-the-project)
* [Current state of the project](#current-state-of-the-project)
* [Plans for the future](#plans-for-the-future)
* [Technologies used](#technologies-used)
* [How to run it?](#how-to-run-it)
* [How to use this API?](#how-to-use-this-api)
    * [Account Management](#account-management)
    * [Expense](#expense)
    * [Goal](#goal)
    * [User Personal Details](#user-personal-details)
    * [Goal Analyser](#goal-analyser)
    * [Expense Optimiser](#expense-optimiser)

# About the project
Oszczedzator3000 is a web application that allows people to see where they make mistakes managing their money and track their expenses. Users also can see if they can achieve some goal they have to pay for and in case they can't, they can try to change their saving habits. Users can compare their expenses with users with silimilar life parameters(salary, age etc.) without seeing who exactly buy what and for how much.

This is only backend part of the project, frontend can be found there. Work is still in progress. This is also a college related project for one of the courses.

# Current state of the project
Currently project has implemented REST api with all the endpoints for dataaccess user related. Project is also connected to H2 database with PostgreSQL ready to be connected in the future. 

Users database is integrated with spring security context. It is possible to login to API with JWT, extend JWT duration and register. JWT is not yet fully finished. Only logged in users can read the api, and also they need to be authorised to see data. Plans for the future are specified below.

# Plans for the future
* Login and registration data validation
* Email verification after creating account
* Redis JWT Secret storage for each user
* Logout from all accounts
* Password change
* Admin API

# Technologies used
* Java 11
* Spring Boot 2.4.4
* H2 Database 
* Spring Data JPA
* Spring Security
* Map Struct 1.4.2
* Lombok 1.18.18
* JJWT 0.9.1

# How to run it?

 Firstly you have to download the repository

```cmd
git clone https://github.com/xNakero/oszczedzator3000-backend.git
 ```

You can run the application in the terminal. To do it you need [maven](https://maven.apache.org/install.html).

In order run run the application go to project directory with the project using
```cmd
cd github-repository-analyser
```
Then you can run the app using 

```
mvn spring-boot:run
```
# How to use this API?

* Application by default uses port 8080, to change it edit ``application.properties`` by adding line ``server.port=n`` where n is chosen port. 
* Values of the json properties in documentation are types that have to be provided to receive 2xx response status. 
* Application requires JWT token in order to access resources


## Account Management

### Data Transfer Objects
#### User
```
{
    "username": string,
    "password": string
}
```
* ``username`` - username of the user, it has to be an email address
* ``password`` - password of the user

#### Jwt 
```
{
    "token": string
}
```
* ``token`` - JWT token, valid for 24 hours

#### Registration 
```
{
    "username": string,
    "password": string,
    "user_personal_details": {
        "salary": double,
        "profession": profession,
        "age": int,
        "sex": sex, 
        "relationship_status": relationship_status,
        "kids": int
    }
}
```
Nothing in this body can be null.
* ``username`` - username of the user, it has to be an email address
* ``password`` - password of the user
* ``user_personal_details`` - can be found at (#user-personal-details)

#### Registration Confirmation
```
{
   "username": string,
   "token_value": string;
}
```
* ``username`` - username of the user, it has to be an email address
* ``token_value`` - token from email

#### Resend token
```
{
   "username": string;
}
```
* ``username`` - username of the user, it has to be an email address

#### Password change
```
{
   "old_password": string,
   "new_password": string;
}
```
* ``old_password`` - old password of the user
* ``new_password`` - new password of the user

#### Forgot password mail
```
{
   "username": string;
}
```
* ``username`` - username of the user, it has to be an email address

#### Set new password
```
{
   "username": string,
   "token": string,
   "new_password": string;
}
```
* ``username`` - username of the user, it has to be an email address
* ``token`` - token from email
* ``new_password`` - new password of the user

### Methods
#### Register - POST method
In order to register in the oszczedzator3000 API you have to go to the endpoint
```
/api/v1/register
```
This endpoint requires [registration](#registration) as request body. If the user was successfully register the application returns 201 HTTP status code, otherwise it returns 409 code or 400. It also sends a 6-digit code at destined email. After registering check out [registration confirmation](#registration-confirmation---post-method)

#### Registration confirmation - POST method
In order to enable the account you have to send a request to this endpoint. 
```
/api/v1/auth
```
It accepts [registration-confirmation](#registration-confirmation) as request body. 

#### Resend token - POST method
In order to have new token to enable the account you have to send a request to this endpoint.
```
/api/v1/new-token
```
It accepts [resend-token](#resend-token) as request body. 

#### Login - POST method
To obtain JWT token user has to login to API. To login you have to go to the endpoint
```
/api/v1/login
```
This endpoint requires [User](#user) as request body. If login was successful it returns [Jwt](#jwt) as a response.

#### Extend token duration - POST method
The token is not infinite. At some point in order to not logout user from a website that uses API the token has to be extended. It can be done by sending a request to the endpoint.
```
/api/v1/token-extension
```
This endpoint returns [Jwt](#jwt) as the response only when the authorization token is valid or not expired.

#### Logout - POST method
To logout of all devices you have to go to the endpoint
```
/api/v1/logout-all
```
If logout was successful it returns appropriate response.

#### Password change - POST method
In order to change password when user is logged in you have to send a request to this endpoint. 
```
/api/v1/change-password
```
It accepts [password change](#password-change) as request body.

#### Forgot password - POST method
In order to change password when user is logged out you have to send a request to this endpoint. 
```
/api/v1/forgot-password
```
It accepts [forgot password](#forgot-password-mail) as request body.

#### Set new password - POST method
In order to set new password when user is logged out you have to send a request to this endpoint. 
```
/api/v1/forgot-password/new-password
```
It accepts [new password](#set-new-password) as request body.

## Expense
Expenses are accessible only for the users that owns them. Users can get, post, patch or delete their expenses. User can get all of his/her expenses or filter them.

### Data Transfer Objects
#### Request with filters
```
{
    "category": string,
    "min_value": double,
    "max_value": double,
    "start_date": YYYY-MM-DD,
    "end_date": YYYY-MM-DD,
    "name": string
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``
* ``min_value`` - minimum value of the filtered expenses
* ``max_value`` - maximum value of the filtered expenses
* ``start_date`` - minimum date from which expenses will be filtered
* ``end_date`` - maximum date from which expenses will be filtered
* ``name`` - name of the searched expense

#### Request with expense data
```
{
    "category": string,
    "name": string,
    "value": double,
    "date": YYYY-MM-DD
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``
* ``name`` - name of the expense
* ``value`` - value of expense
* ``date`` - date when the expense was recorded

#### Expense Response
```
{
    "category": string,
    "name": string,
    "value": double,
    "date": YYYY-MM-DD,
    "expense_id": long
}
```
All of properties are the same as in [Request with expense data](#request-with-expense-data) with one addition

``expense_id`` - unique id of an expense

### Methods

All requests but POST and DELETE return one or more [Expense response](#expense-response).

#### GET all expenses
```
/api/v1/expenses
```

There are also optional parameters
* ``page`` - id of page, starting from 0, default is 0
* ``size`` - size of page, 10 by default

#### GET filtered expenses
```
/api/v1/expenses/filtered
```
This request has the same optional parameters as unfiltered one, however it accept a request body. This body is [Request with filters](#request-with-filters)

#### POST expense
```
/api/v1/expenses
```
This request accepts [Request with expense data](#request-with-expense-data) as request body. Expense can be posted only if every 
property is filled. 

#### PATCH expense
```
/api/v1/expenses/{expense_id}
```
* ``expense`` - unique id of an expense
This request accepts [Request with expense data](#request-with-expense-data) as request body. Fill only properties that you want to have updated. User can update only his expenses. 

#### DELETE expense
```
/api/v1/expenses/{expense_id}
```
* ``expense`` - unique id of an expense
User can delete only his/her expenses. 

## Goal
Goals are accessible only for the users that owns them. Users can get, post, patch or delete their goals. User can get all of his/her goals or filter them.

### Data Transfer Objects
#### Goal request with filters
```
{
    "category": string,
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``

#### Request with goal data
```
{
    "category": string,
    "name": string,
    "price": double,
    "target_date": YYYY-MM-DD
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``
* ``name`` - name of the goal
* ``price`` - value of goal that user what to achieve
* ``target_date`` - date when the user what to achieve the goal

#### Goal Response
```
{
    "category": string,
    "name": string,
    "price": double,
    "target_date": YYYY-MM-DD,
    "goal_id": long
}
```
All of properties are the same as in [Request with goal data](#request-with-goal-data) with one addition

``goal_id`` - unique id of a goal

### Methods

All requests but POST and DELETE return one or more [Goal response](#goal-response).

#### GET all goals
```
/api/v1/goals
```

There are also optional parameters
* ``page`` - id of page, starting from 0, default is 0
* ``size`` - size of page, 10 by default

#### GET filtered goals
```
/api/v1/goals/filtered
```
This request has the same optional parameters as unfiltered one, however it accept a request body. This body is [Request with filters](#goal-request-with-filters)

#### POST goal
```
/api/v1/goals
```
This request accepts [Request with goal data](#request-with-goal-data) as request body. Goal can be posted only if every 
property is filled. 

#### PATCH goal
```
/api/v1/goals/{goal_id}
```
* ``goal_id`` - unique id of an goal
This request accepts [Request with goal data](#request-with-goal-data) as request body. Fill only properties that you want to have updated. User can update only his goals. 

#### DELETE goal
```
/api/v1/goals/{goal_id}
```
* ``goal_id`` - unique id of an goal
User can delete only his/her goals. 

## User Personal Details
### Data Transfer Objects
#### User Personal Details Request/Response
```
{
    "salary": double,
    "profession": string,
    "age": int,
    "sex": string,
    "relationship_status": string,
    "kids": int
}
```
* ``salary`` - salary of an individual
* ``profession`` - one of professions that are available at the endpoint ``api/v1/enums/profession``
* ``age`` - age of a user
* ``sex`` - one of genders that are available at the endpoint ``api/v1/enums/sex``
* ``relationship_status`` - one of relationship statuses that are available at the endpoint ``api/v1/enums/relationship_status``
* ``kids`` - number of kids owned by a user 

### Methods

All responses return [User Personal Details Request/Response](#user-personal-details-requestresponse).

There are availible 3 HTTP methods - GET, POST, PATCH. User can only delete his personal details when he deletes his/her account. User can have access only to his/her personal details. 

All methods are available at the endpoint
```
/api/v1/details
```

#### GET user personal details
Simply returns personal details of a user.

#### PATCH user personal details
Fill only properties that you want to have updated.

## Goal Analyser
Goal Analyser analyses whether user can afford a goal based on his expenses in chosen period of time. 

### Data Transfer Objects
#### Goal Analyser Request
```
{
    initial_contribution: double,
    start_date: YYYY-MM-DD,
    end_date: YYYY-MM-DD
}
```
* ``initial_contribution`` - initial money that can be contributed towards a goal
* ``start_date`` - start date to calculate balance
* ``end_date`` - end date to calculate balance

#### Goal Analyser Response
```
{
    "can_achieve": boolean,
    "money_to_collect": double,
    "average_daily_possible_savings": double,
    "average_daily_necessary_savings": double,
    "can_achieve_before_end_date": boolean
}
```
* ``can_achieve`` - if user earned more than spent in this timeperiod then it's true, else it's false
* ``money_to_collect`` - amount of money that has to be collected after deducting initial contribution
* ``average_daily_possible_savings`` - average amount of money that user can save daily
* ``average_daily_necessary_savings`` - average amount of money that user has to save daily in order to be able to save for this item
* ``can_achieve_before_end_date`` - if user daily saves more or equal to the necessary amount of money than it's true, else it's false

### Methods 
Goal Analyser has only one HTTP method - GET. It can be reached at the endpoint
```
/api/v1/goals/{goal_id}/analyser
```
* ``goal_id`` - unique id of a goal, user can access only his goals

This request accepts [Goal Analyser Request](#goal-analyser-request) as request body and returns [Goal Analyser Reponse](#goal-analyser-response) as response.

## Expense Optimiser
Expense Optimiser analyses user's expenses and returns informations whether user is better at spending money than other people with chosen parameters. The categories have to be present in order to return a response data.
### Data Transfer Objects
#### Filtration Expense Optimiser Request
```
{
    "salary": boolean,
    "profession": boolean,
    "age": boolean,
    "sex": boolean,
    "relationship_status": boolean,
    "kids": boolean,
    "start_date": YYYY-MM-DD,
    "end_date": YYYY-MM-DD,
    "categories": [
        string
    ]
}
```
* ``salary`` - salary +/- 500 to filter other users
* ``profession`` - profession that filtered users have. Professions are available at the endpoint ``api/v1/enums/profession``
* ``age`` - age +/- 2 that filtered users have
* ``sex`` - gender that filtered users have. Genders are available at the endpoint ``/api/v1/enums/sex``
* ``relationship_status`` - relationship status that filtered users have. Relationship statuses are available at the endpoint ``/api/v1/enums/relationship_status``
* ``kids`` - amount of kids that filtered users have to have
* ``start_date`` - start date to filter expenses of other users by
* ``end_date`` - end date to filter expenses of other users by
* ``categories`` - categories are available at the endpoint ``/api/v1/enums/category``. Categories should be sent as an array, and there can be multiple of them. 

#### Expense Optimiser Response
Response has two arrays:
* ``user_data`` - data of the user that made request
* ``similar_users_data`` - data of the filtered users
```
{
    "user_data": [
        {
            "category": string,
            "average_spent": double,
            "expense_count": int
        },
        ...
    ],
    "similar_users_data": [
        {
            "category": string,
            "average_spent": double,
            "expense_count": int
        },
        ...
    ]
}
```
* ``category`` - categories are available at the endpoint ``/api/v1/enums/category``. It is an information to which category this array is related to
* ``average_spent`` - average amount of money spent on this category
* ``expense_count`` - number of expenses that this statistic is based on

### Methods
There is only one HTTP method - GET. It can be reached at the endpoint
```
/api/v1/optimiser
```
This request accepts [Filtration Expense Optimiser Request](#filtration-expense-optimiser-request) as request body and returns as a response [Expense Optimiser Response](#expense-optimiser-response).

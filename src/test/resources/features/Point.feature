@Point @CRUD
Feature: 
  As a admin
  I want to manage points
  for use these points to register locations and link these with routes

  Background: 
    Given these registered points:
    	| name |
    	| A    |
    	| B    | 
    	| C    |
 
  @Create @Success
  Scenario: register a new point
		Given a new point with name "D"
		When register this point 
		Then the point will be successfully registered


  @Create @Fail
  Scenario: fail when register a new route without name
		Given a new point without name
		When register this point 
		Then will fail with status code 400
		And will fail with message "Point name is required."
		
  @Create @Fail
  Scenario: fail when register a new route with empty name
		Given a new point with name ""
		When register this point 
		Then will fail with status code 400
		And will fail with message "Point name is required."
		
  @Create @Fail
  Scenario: fail when register a new route with a existing name
		Given a new point with name "A"
		When register this point 
		Then will fail with status code 400
		Then will fail with message "already exists with label `Point` and property `name` = 'A'"
		
	@Delete @Fail
	Scenario: delete a inexistent point
		Given that no exists a point with id 999
		When delete the point with id 999 
		Then will fail with status code 404
		
	@Delete @Success
	Scenario: delete a existent point
		When delete the point with name "C"
		Then the point "C" will be successfully deleted 
		
	@Update @Success
	Scenario: update a existing point
		When update point with name "A" to name "X"
		Then the point will be successfully updated
		
	@Update @Fail
	Scenario: fail when update point to another existing name
		When update point with name "B" to name "A"
		Then will fail with message "already exists with label `Point` and property `name` = 'A'"
		
	@Find @Fail
	Scenario: find a unexistent point by id
		Given that no exists a point with id 999
		When find a point with id 999
		Then will fail with status code 404
		
	@Find @Fail
	Scenario: find a unexistent point by name
		Given that no exists a point with name "unexistent"
		When find a point with name unexistent
		Then will fail with status code 404
		

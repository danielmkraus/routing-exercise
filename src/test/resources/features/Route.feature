@Route @CRUD
Feature: 
  As a admin
  I want to manage routes
  for use these routes to define paths

  Background: Create base points
    Given these registered points:
    	| name |
    	| A    |
    	| B    | 
    	| C    |
    	| D    | 
 
  @Create @Fail 
  Scenario Outline: fail when register a route without field
	 	When register a route without <field>
	 	Then will fail in route register with message "<message>"
	 	
	 	Examples:
	 		| field       | message                        |
	 		| time        | Route time is required.        |
	 		| cost        | Route cost is required.        | 
	 		| origin      | Route origin is required.      | 
	 		| destination | Route destination is required. |
	
  @Create @Fail 
  Scenario: fail when register a route with negative time
	 	When register a route time -1
	 	Then will fail in route register with message "Route time cannot be negative."
	
  @Create @Fail 
  Scenario: fail when register a route with negative cost
	 	When register a route cost -1
	 	Then will fail in route register with message "Route cost cannot be negative."
	
  @Update @Success
  Scenario: register a new route and edit
		Given this registered route:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
		When edit this registered route with time 2 and cost 2
		Then the routes will be successfully edited 
 
 
  @Update @Fail 
  Scenario Outline: fail when edit a route without field
	 	Given this registered route:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
		When edit this registered route without <field>
	 	Then will fail in route register with message "<message>"
	 	
	 	Examples:
	 		| field       | message                        |
	 		| time        | Route time is required.        |
	 		| cost        | Route cost is required.        | 
	 		| origin      | Route origin is required.      | 
	 		| destination | Route destination is required. |
 
  @Create @Fail 
  Scenario: fail when register a route with negative time
  		Given this registered route:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
	 	When edit this route time -1
	 	Then will fail in route register with message "Route time cannot be negative."
	
  @Create @Fail 
  Scenario: fail when register a route with negative cost
	 	Given this registered route:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
		When edit this route cost -1
	 	Then will fail in route register with message "Route cost cannot be negative."
	
  @Delete @Success
  Scenario: register a new route and retrieve paths
		Given this registered route:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
		When delete this registered route 
		Then the routes will be successfully deleted 
 
  @Create @Success
  Scenario: register a new route and retrieve paths
		When register these routes:
			| origin | destination | time | cost | 
			| A      | B           | 1    | 1    | 
			| B      | C           | 1    | 1    | 
			| C      | D           | 1    | 1    | 
			| A      | D           | 10   | 1    | 
		Then the routes will be successfully registered
		
		And the cheapest path from A to D has time 10 and cost 1 with this path:
			| name |
			| A    |
			| D    |
		And the fastest path from A to D has time 3 and cost 3 with this path:
			| name |
			| A    |
			| B    | 
			| C    |
			| D    |
		And the cheapest path when time cost is 10 from A to D has time 3 and cost 33 with this path:
			| name |
			| A    |
			| D    |
		
		
		
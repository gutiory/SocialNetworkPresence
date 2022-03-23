## Coding Task - Social Network Presence ##

A third party provider has exposed an end-point for you that retrieves a graph representation using JSON like the following:

Requests:
GET https://my-third-party/facebook
GET https://my-third-party/twitter

Example response:
```json
{
"sn": "facebook",
"people": [{"name":"John"},{"name":"Harry"},{"name":"Peter"}, {"name": "George"}, {"name": "Anna"}],
"relationships": [
    {"type": "HasConnection", "startNode": "John", "endNode": "Peter"},
    {"type": "HasConnection", "startNode": "John", "endNode": "George"},
    {"type": "HasConnection", "startNode": "Peter", "endNode": "George"},
    {"type": "HasConnection", "startNode": "Peter", "endNode": "Anna"}
]
}
```

In the example response above we can count the connections by their **minimum** degrees of separation like this:

|        | 1 degree | 2 degrees |
|--------|----------|-----------|
| John   | 2        | 1         |
| Peter  | 3        | 0         |
| George | 2        | 1         |
| Harry  | 0        | 0         |
| Anna   | 1        | 2         |

You have been assigned to develop a Rest API that will consume the one above.

### User Stories ###

As a user, I want to query how many people are not connected to anyone for the given social network so I know who to propose new connections to.

    Given a social network name Facebook
    And a full Facebook graph
    Return count of people with no connections


As a user, I want to query how many people are connected to a given person by 1 or 2 degrees of separation for all social networks (facebook and twitter) so I understand her/his social influence.

    Given a person name Peter
    And a Facebook graph for Peter
    And a Twitter graph for Peter
    Return count of connections of 1 degree + count of connections of 2 degree 

### Hints ###
- Define a trait that will be interface used against third party API, you don't need to provide any implementation.
- Third party endpoint can return a graph representation for just one social network per request.

### Bonus points ###
- Add a cache  for third party responses

### Considerations ###
- We are looking for a solution where the candidate shows their skills around clean code and functional programming.
- We do not expect you to host this Rest API or have it fully working, just a test suite that we can run using your preferred build tool.
- We expect you to spend no more than a few hours on this task so we can discuss your prioritisation, design approach and quality aspects in the technical interview.

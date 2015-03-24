# PROPOSED MESSAGE FORMAT #
```
ContactSwap:COMMAND:TAG:Data:TAG:Data...:
```
COMMAND -> Query, Contact, Friend

TAG -> Name, Phone, NotFound, Accept, Decline

Replace " " with "$"

# For example: #

### The Search ###
```
ContactSwap:Query:Name:Rob$McColl:
```
### Results if Found ###
```
ContactSwap:Contact:Name:Rob$McColl:Phone:2054220909:
```
### Results if Not Found ###
```
ContactSwap:Contact:Name:Rob$McColl:NotFound:
```
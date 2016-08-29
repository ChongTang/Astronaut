module customerOrderObjectModel
open Declaration

one sig Customer extends Class{}{
attrSet = customerID+customerName
id=customerID
isAbstract = No
no parent
}

one sig customerID extends Integer{}
one sig customerName extends string{}

one sig Order extends Class{}{
attrSet = orderID + orderValue
id=orderID
isAbstract = No
no parent
}
one sig orderID extends Integer{}
one sig orderValue extends Real{}

one sig CustomerOrderAssociation extends Association{}{
src = Customer
dst = Order
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig PreferredCustomer extends Class{}{
attrSet = discount
one parent
parent in Customer
isAbstract = No
id=customerID
}
one sig discount extends Integer{}

pred show{}
run show for 16

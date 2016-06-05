module CustomerOrder
open Declaration

one sig GoldenCustomer extends Class{}{
attrSet = goldDiscount
one parent
parent in PreferredCustomer
id = customerID
isAbstract = No
}

one sig goldDiscount extends Real{}

one sig Customer extends Class{}{
attrSet = customerID+customerName
id=customerID
no parent
isAbstract = No
}

one sig customerID extends Integer{}
one sig customerName extends string{}

one sig Order extends Class{}{
attrSet = orderID+orderPrice+newAttr
id=orderID
no parent
isAbstract = No
}

one sig orderID extends Integer{}
one sig orderPrice extends Integer{}
one sig newAttr extends Bool{}

one sig PreferredCustomer extends Class{}{
attrSet = discount
one parent
parent in Customer
id = customerID
isAbstract = No
}

one sig discount extends Real{}

one sig CustomerOrderAssociation extends Association{}{
src = Customer
dst= Order
src_multiplicity = ONE
dst_multiplicity = MANY
}

pred show{}
run show for 17

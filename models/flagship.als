module decider
open Declaration


one sig User extends Class{}{
attrSet = userID+userName
id=userID
isAbstract = No
no parent
}

one sig userID extends Integer{}
one sig userName extends string{}


one sig Document extends Class{}{
attrSet = documentID+title
id=documentID
isAbstract = No
no parent
}

one sig documentID extends Integer{}
one sig title extends string{}


one sig Background extends Class{}{
attrSet = backgroundID+backgroundName
id=backgroundID
isAbstract = No
no parent
}

one sig backgroundID extends Integer{}
one sig backgroundName extends string{}


one sig Category extends Class{}{
attrSet = categoryID+categoryName
id=categoryID
isAbstract = No
no parent
}

one sig categoryID extends Integer{}
one sig categoryName extends string{}

one sig Group extends Class{}{
attrSet = groupID+groupName
id=groupID
isAbstract = No
no parent
}

one sig groupID extends Integer{}
one sig groupName extends string{}

one sig Revision extends Class{}{
attrSet = revisionID+revisionName
id=revisionID
isAbstract = No
no parent
}

one sig revisionID extends Integer{}
one sig revisionName extends string{}


one sig GroupUserAssociation extends Association{}{
src = Group
dst = User
src_multiplicity = MANY
dst_multiplicity = MANY
}

one sig CategoryGroupAssociation extends Association{}{
src = Group
dst = Category
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig CategoryUserAssociation extends Association{}{
src = User
dst = Category
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig CategoryBackgroundAssociation extends Association{}{
src = Background
dst = Category
src_multiplicity = ONE
dst_multiplicity = MANY
}


one sig DocumentCategoryAssociation extends Association{}{
src = Category
dst = Document
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig DocumentUserAssociation extends Association{}{
src = User
dst = Document
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig RevisionDocumentAssociation extends Association{}{
src = Document
dst = Revision
src_multiplicity = ONE
dst_multiplicity = MANY
}

one sig RevisionUserAssociation extends Association{}{
src = User
dst = Revision
src_multiplicity = ONE
dst_multiplicity = MANY
}

pred show{}
run show for 35

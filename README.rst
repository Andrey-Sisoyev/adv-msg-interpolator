A message interpolator based on a seriously rewritten MI from hibernate-validator v4.1.0.Final, using algorithm a bit different one than specified in JSR303. A more advanced approach, where:

 **(A)** Both RBs (resource bundle) and annotations ones may be mutually recursivaly resolved. While original algorithm allowed annotation properties resolution only once, nonrecursively and in the end of algorithm. The rationale for this advancement is mainly in the presentation of enums and ADTs.

 **(B)** RB name may be specified by the RB property like so: {${rb=MyBusinessObj_messages}violations.businessRule_1.message} . This approach allows incapsulation of application specific validations by the business objects, while in current JSR303 you are bound to put everything in a messy one file.

 **(C)** Annotations properties are differentiated from RB properties: {$@my_favoriteannotation_property} , but legacy syntax is still supported for backward compatibility - if {property} isn't resolved in default RB's, annotation properties are checked for such prop name.


New resolution algorithm is mostly backward compatible (except for it may resolve more than JSR303 would do, which case is hard to imagine):

 **(1)** Resolve all the RB property recursively:

 **(1.1)** If RB name specified (by the property) look for it only in specified RB

 **(1.2)** Else look in the user's ValidationsMessages RB (or what he've configured) and if not found, look for it in the "org.hibernate.validator.ValidationMessages" inner core RB

 **(2)** If no more properties resolution possible, resolve annotations properties. On each resolved annotation fragment go recursively to (1).


Step **(1)** has 3 caches in use, assumming, that resource bundle content would never change dinamically (while application is working):

 **1.** {RBName :: String, Locale} -> ResourceBundle

 **2.** {RBName :: String, PBPropertyKey :: String, Locale} -> {ResourceBundle, ResolvedMessage :: String, IsResolved :: Boolean}

 **3.** {RBProperty :: String, Locale, DefaultRBs :: ResourceBundle[]} -> ResolutionResult :: String


Few bonuses are:

 \*\* validators @Cmp and @DoubleCmp, that generalizes Min, Maxs, as well as EQ, NEQ, GT and LT operations.

 \*\* validator @AlwaysFail for developement convenience

 \*\* localization of core validation messages for languages: Russian and Latvian.


Link:

 \---> Example and discussion: 

           http://forum.hibernate.org/viewtopic.php?f=26=1010669=2444299#p2444299


 \---> Property files:

  \--->-> localized core messages:

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/native2ascii/properties/org/hibernate/validator 

  \--->-> validation messages for new validators (like @Cmp):

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/native2ascii/properties/home/lang/jsr303mod/validator 


 \---> MessageInterpolator impl classes:

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/java/home/lang/jsr303mod/rbmsginterpolator


**Everything is well-tested and ready for industrial use, so, you are welcome.**
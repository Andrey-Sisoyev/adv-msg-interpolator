A message interpolator based on a seriously rewritten MI from hibernate-validator v4.1.0.Final, using algorithm a bit different one than specified in JSR303. A more advanced approach, where:

 **(A)** Both RBs (resource bundle) and (validator's) annotation properties may be mutually recursivaly resolved. While original algorithm allowed annotation properties resolution only once, nonrecursively and in the end of algorithm. The rationale for this advancement is mainly in the presentation of enums and ADTs.

 **(B)** RB name may be specified by the RB property like so: **{${rb=MyBusinessObj_messages}violations.businessRule_1.message}** . This approach allows incapsulation of application specific validation messages by the business objects, while in current JSR303 you are bound to put everything in a messy one file.

 **(C)** Annotations properties are differentiated from RB properties: **{\$\@my_favorite_annotation_property}** , but legacy syntax is still supported for backward compatibility - if **{property}** isn't resolved in default RB's, annotation properties are checked for such prop name.


New resolution algorithm is mostly backward compatible, except for it may resolve more than JSR303 would do (which case is hard to imagine):

 **(1)** Resolve all the RB properties recursively:

 **(1.1)** If RB name is specified (by the property, like so: **{\$\{rb=myrb_name}property_id}**)) look for it only in specified RB

 **(1.2)** Else look in the user's *ValidationsMessages* RB (or what he've configured) and if not found, look for it in the "*org.hibernate.validator.ValidationMessages*" inner core RB

 **(2)** If no more properties resolutions possible, resolve annotation properties. On each resolved annotation fragment go recursively to **(1)**.


Step **(1)** has 3 caches in use, assumming, that resource bundle content would never change dynamically (while application is working):

 **1.** {RBName :: String, Locale} -> ResourceBundle

 **2.** {RBName :: String, RBPropertyKey :: String, Locale} -> {ResourceBundle, ResolvedMessage :: String, IsResolved :: Boolean}

 **3.** {RBProperty :: String, Locale, DefaultRBs :: ResourceBundle[]} -> ResolutionResult :: String


Few bonuses are:

 \*\* validators **\@Cmp** and **\@DoubleCmp**, that generalizes @Min, @Max, as well as EQ, NEQ, GT and LT operations.

 \*\* validator **\@AlwaysFail** for developement convenience

 \*\* localization of core validation messages for languages: Russian and Latvian.


Links:

 \---> Example and discussion: 

           http://forum.hibernate.org/viewtopic.php?f=26&t=1010669


 \---> Property files:

  \--->-> localized core messages:

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/native2ascii/properties/org/hibernate/validator 

  \--->-> validation messages for new validators (like **\@Cmp**):

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/native2ascii/properties/home/lang/jsr303mod/validator 


 \---> MessageInterpolator impl classes:

           http://github.com/Andrey-Sisoyev/adv-msg-interpolator/tree/master/src/main/java/home/lang/jsr303mod/rbmsginterpolator


**Everything is well-tested and ready for industrial use, so, you are welcome.**
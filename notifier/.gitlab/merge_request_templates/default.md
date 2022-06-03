_Fill all the blanks and remove italic comments before submitting the MR._

## User Story / Issue (Jira)

_Replace XXXX (in text and link url) by the Jira Issue code / title._

Code : [LOT2C-XXXX](http://jira.pointp.saint-gobain.net/browse/LOT2C-XXXX)
Title : XXXX

### Sub-task

_Replace XXXX (in text and link url) by the Jira Sub-task code / title, or remove this chapter if irrelevant._

Code : [LOT2C-XXXX](http://jira.pointp.saint-gobain.net/browse/LOT2C-XXXX)
Title : XXXXX

## This is a ...

_Put an `x` in the boxes that apply._

- [ ] fix related to a business issue
- [ ] fix related to a technical issue
- [ ] business feature
- [ ] technical feature
- [ ] part of a feature, which should be merged for collaboration reasons

## Further comments

_If this is a relatively large or complex or important change, kick off the discussion by explaining why you chose the solution you did and what alternatives you considered, etc..._

## Review checklist (DoD)

_These checklist is for code review, in order to validate the MR compliance to the "Definition of Done"._

_The feature team technical leader will put an `x` in the boxes that apply after verifying its compliance._

_After a first review (which could be done alongside with you), if any of its isn't compliant, the feature team technical leader
will put an `-` in all the boxes that apply, put a `[WIP]` in this MR title, and reassign it to you. You'll have to fix **all** of its,
remove the `-` from the boxes, remove the `[WIP]` in the title, and reassign the MR to the feature team technical leader,
which will then review in details your code in order to permit to "check" the last box._

_This process should only occur once in most cases._

### Tests

- [ ] new code has a unit tests coverage of 80% or more
- [ ] each associated acceptance specification is covered by at least one acceptance test
- [ ] there isn't any ignored (`@ignore`) test
- [ ] there isn't any "broken" test

### Documentation

- [ ] each modification or addition that isn't private is well documented in javadoc format
- [ ] each mocked data is preceded by a generic currency sign ( `¤` )
- [ ] each future development (eg. a mocked data, or a part of a feature specified in another US) is commented using one of the codetags from [PEP-350](http://legacy.python.org/dev/peps/pep-0350/#mnemonics)

### Code delta

- [ ] each previously commented issues (ie. FIXME, TODO, etc... ) related to this feature has been resolved
- [ ] new code doesn't duplicate previously existing code from the project or any library

### Quality

- [ ] new code respect [SGDBF Java Style Guides](http://confluence.pointp.saint-gobain.net/pages/viewpage.action?pageId=88014895)
- [ ] all additions and motifications are complicant with the feature team technical leader expectations

### Reviewers

...



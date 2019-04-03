# Contribution Guide

## Installing the Development Environment
- LIMO is primarily developed in Eclipse using GWT extensions. See <https://github.com/purduedb/LIMO/blob/master/LIMO_InstallationGuide.md> for detailed installation instructions

## New Features and Issues
- Bugs and enhancements will be tracked using Github's issue tracker system found here: <https://github.com/purduedb/LIMO/issues>
- Issues will be prioritized, assigned, and tracked in the Kandban-style LIMO project page found here: <https://github.com/orgs/purduedb/projects/1>

## Choosing a Feature to Work on
- TODO - Deciding requirements, data structures

## Branches
- We will be using a stable/dev + feature branch organization
  - Two permanent branches - `master` and `dev`
    - `master` contains the last known stable working LIMO release
    - `dev `contains new features and documentation that has not been packaged into a release and deployed on our test server
  - One branch per in-progress feature
    - Each researcher is expected to have a few branches per semester as they add features to LIMO
    - Features may not be visable to the user - such as changes to internal data structures

## Pull Requests
- After you have completed a new feature on your own branch you must open a pull request to add your feature to the main codebase
- It is expected that you have run pylint and YAPF on your code, all test cases pass, and you have written additional unit tests for pylint that cover the new features that you have added (See Conventions and Testing below)
- Once a branch has been properly tested and linted, please open a pull request and then post a message in the LIMO group describing the change. During the weekly LIMO meeting, your code will be reviewed by the team

## Code Conventions
- We would like LIMO to follow PEP-8 Style conventions. When adding new code, please follow these conventions
  - Style guide can be found here: <https://www.python.org/dev/peps/pep-0008/>
- In order to enforce these conventions, we will use pylint to check your code for deviations, and YAPF to attempt to automatically fix these deviations
- To execute the linter from the command line, use the command `pylint war/limo2.31.py` while in the root project directory. This will automatically use the project's pylint configuration file
- To execute the yapf autoformatting tool, use the command `yapf war/limo2.31.py -i` while in the root project directory

## Refactoring:
- The LIMO codebase does not follow our code conventions. Please do you part and add comments and fix glaring issues as you come across them
- Refactoring is done in dev branch
  - While acclimating yourself to LIMO, stay in the dev branch, add comments, and commit
  - One the LIMO's biggest issues in the code base is the lack of commenting. Please help fix this when you are exploring the code

## Testing
- LIMO aims to use two methods of testing
  - Postman is used for system and performance testing. This program sends POST requests to a running LIMO server instance
    - To execute the Postman tests, please use the Postman test runner in the Postman GUI after importing the configurations found in the `tools` directory
    - If you would like to execute these tests via CLI and $REPODIR is the directory where you cloned the repository, please execute `newman run $REPODIR/tools/postman/LIMO.postman_collection.json -e $REPODIR/tools/postman/LIMO\ Local.postman_environment.json`
      - Make sure you have installed newman, the CLI version of postman while installing the LIMO dev environment
  - pytest is used for python-specific unit tests
    - In this context, unit tests will look at external and internal function definitions individually
=======

## Testing
- LIMO aims to use two methods of testing
  - Postman is used for system and performance testing. This program sends POST requests to a running LIMO server instance
    - TODO: How to install and use
  - pytest is used for python-specific unit tests
    - In this context, unit tests will look at external and internal function definitions individually

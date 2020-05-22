# ircs-bangalore-app

Follow the steps given below to contribute
**Fork this repo**
**Clone your forked repo**:
Replace **${YOUR_GITHUB_USERNAME}** with your github username before running the below code

 ```$ git clone https://github.com/${YOUR_GITHUB_USERNAME}/ircs-bangalore-app```
 
  
  **Add & fetch this repo as a remote named `upstream`:**
  
  ```$ git remote add -f upstream https://github.com/koala25/ircs-bangalore-app```
  
  **Create and move to a new local branch based on our current `master` branch:**
  Replace **${BRANCH_NAME}** below with whatever you feel is suitable for the task you are working on.

``` $ git checkout -b ${BRANCH_NAME} upstream/master```

**Publish and push your local branch to your forked repo:**

```$ git push -u origin ${BRANCH_NAME}```

...*have a play around*... Go through the code architecture and see if you understand everything.

You can now make pull requests from your pushed branches.

You can keep your forked repo's branches in-sync with our upstream changes. **_We recommend doing this before making or requesting any reviews on PRs_**.

**Replay all your commits on top of our current `master` branch:**

``` $ git pull --rebase upstream master ```

**Overwrite your personal forked repo's branch with your synced local branch state:**

``` $ git push --force ```

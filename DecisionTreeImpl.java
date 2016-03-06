import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 * 
 * You must add code for the 1 member and 4 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
  private DecTreeNode root;
  //ordered list of class labels
  private List<String> labels; 
  //ordered list of attributes
  private List<String> attributes; 
  //map to ordered discrete values taken by attributes
  private Map<String, List<String>> attributeValues; 
  
  /**
   * Answers static questions about decision trees.
   */
  DecisionTreeImpl() {
    // no code necessary this is void purposefully
  }

  /**
   * Build a decision tree given only a training set.
   * 
   * @param train: the training set
   */
 // private static int stackOverInd = 0;
  DecisionTreeImpl(DataSet train) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    if(this.labels.size() == 0){
    	this.root = null;
    }
    else{
    	this.root = BuildTree(train.instances, this.attributes, this.labels, new DecTreeNode(GetMajorityClass(train.instances), null, null, true), null);
    }

  }

  private DecTreeNode BuildTree(List<Instance> examples, List<String> attrs, List<String> labs, DecTreeNode defaltN, String parentAttribVal){
	  
	//  stackOverInd ++;
	  if (examples.isEmpty()){
		  //TODO change defaultN to be a label and then construct the new treenode from that information.
		  defaltN.parentAttributeValue = parentAttribVal;
		  return defaltN;		  
	  }
	  //All objects under the root node have only one label so you know for certain how to classify all of the examples. 
	  if(AllExamplesHaveSameLabel(examples)){
		  return new DecTreeNode(examples.get(0).label, null, parentAttribVal, true);
	  }
	  if(attrs.isEmpty()){
		  //TODO figure out what the attribute should be at this point. I don't think it has an attribute. And then any node that does have an attribute I think won't have a label.	
		  return new DecTreeNode(GetMajorityClass(examples), null, parentAttribVal, true);
	  }
 

	  //find best attribute to split the data on.
	  int bestAttr = GetBestAttributeIndex(examples, attrs, labs);
	  String chosenAttr = attrs.get(bestAttr);

	  
	  //The node that will be returned.
	  DecTreeNode gandalf = new DecTreeNode(null, chosenAttr, parentAttribVal, false);
	  
	  //remove the chosen attribute from the list and recurse on all possible attribute values for specified attribute.
	  List<String> tempAttributes = new ArrayList<>(attrs);
	  tempAttributes.remove(tempAttributes.indexOf(chosenAttr));
	  
	  //make sure you get attribute values that don't have any test data for them. You still want to classify those classes.
	  List<String> allAttrsValues = attributeValues.get(chosenAttr);
	  for (int curAttr = 0; curAttr < allAttrsValues.size(); curAttr++){
		  List<Instance> instancesWithAttr = GetExamplesWithSpecificAttirbute(examples, getAttributeIndex(chosenAttr), allAttrsValues.get(curAttr));
		  gandalf.children.add(BuildTree(instancesWithAttr, tempAttributes, labs, new DecTreeNode(GetMajorityClass(examples), null, allAttrsValues.get(curAttr), true), allAttrsValues.get(curAttr) ));//chosenAttr
	  }
	  
	  return gandalf;
	  
	  /*TODO First pick an attribute to split these bad boys up by.
	   * Then create the new node splitting the other examples by the selected attribute
	   * And add children to the current node based on recursive calls on subset of examples split up by the attribute. 
	  */
	  
	  
  }
  
  /**
   * Finds the index of the best attribute for the current node. 
   * The best index is the one that produces the greatest information gain.
   * @param examples
   * @param attrs
   * @param labs
   * @return
   */
  private int GetBestAttributeIndex(List<Instance> examples, List<String> attrs, List<String> labs){
	  int bestAttrInd = -1;
	  double bestInfoGain = -1.0;
	  for(int attrInd = 0; attrInd < attrs.size(); attrInd ++){
		  
		  double curGain = CalcInformationGain(examples, labs, attrs.get(attrInd)); 
		  if(curGain > bestInfoGain){
			  bestAttrInd = attrInd;
			  bestInfoGain = curGain;
		  }
	  }
	  return bestAttrInd;
  }
  

  private double CalcInformationGain(List<Instance> examples, List<String> labs, String selectedAttrib){
	  double currentEntropy = CalcEntropy(examples, labs);
	  double infoGain = currentEntropy;
	  
	  //TODO figure out if this actually gives the index of the attribute for an Instance type.
	  int attribInd = getAttributeIndex(selectedAttrib);
	  List<String> visitedAttributes = new ArrayList<>();
	  String currAtrib = "";
	  for (int exInd = 0; exInd < examples.size(); exInd++){
		  currAtrib = examples.get(exInd).attributes.get(attribInd);
		  if(!visitedAttributes.contains(currAtrib)){
			  //mark the specific attribute visited.
			  visitedAttributes.add(currAtrib);
			  //Get all of the examples that have currAttrib and calc the entropy of that.
			  List<Instance> exWithAttrib = GetExamplesWithSpecificAttirbute(examples, attribInd, currAtrib);
			  double fractionWithAttr = ((double) exWithAttrib.size())/ examples.size();
			  
			  infoGain -= fractionWithAttr*CalcEntropy(exWithAttrib, labs);
		  }
		  
	  }
	  return infoGain;
  }
  
  /**
   * Calculates the entropy of the current list of examples.
   * @param examples
   * @param labs
   * @return
   */
  private double CalcEntropy(List<Instance> examples, List<String> labs){
	  double entropy = 0;
	  int total = examples.size();
	  
	  for(int labInd = 0; labInd < labs.size(); labInd ++){
		  double probabilityLab = (double) GetCountOfExamplesWithLabel(examples, labs.get(labInd));
		  if(probabilityLab != 0){
			  probabilityLab = probabilityLab/total;
			  entropy -= probabilityLab* (Math.log(probabilityLab)/Math.log(2));
		  }
	  }
	  
	  return entropy;
  }
  
  /**
   * Does exactly what it says. Counts the number of examples with the specified label.
   * @param examples
   * @param label
   * @return
   */
  private int GetCountOfExamplesWithLabel(List<Instance> examples, String label){
	  int count = 0;
	  
	  for(int exInd = 0; exInd < examples.size(); exInd++){
		  if (examples.get(exInd).label.equals(label)){
			  count++;
		  }
	  }
	  return count;
  }
  
  private List<Instance> GetExamplesWithSpecificAttirbute(List<Instance> examples, int attributeIndex, String attrib){
	  List<Instance> exsWithAttribute = new ArrayList<>(); 
	  for(int exInd = 0; exInd < examples.size(); exInd++){
		  if(examples.get(exInd).attributes.get(attributeIndex).equals(attrib)){
			  exsWithAttribute.add(examples.get(exInd));
		  }
	  }
	  return exsWithAttribute;
  }
  
  /**
   * Finds the majority class in the list of examples
   * @param examples
   * @return
   */
  private String GetMajorityClass(List<Instance> examples){
	  //TODO Try to optimize using a map structure. For now it can run in O(N^2).
	  //The label that the majority of the examples are. I think the label means class here.
	  //TODO figure out what should happen if there are ties.
	  
	  String majorityLab = "";
	  int majorityCount = 0;
	  
	  //Go through each label that exists in the examples and count how many examples have that label.
	  for(int labInd =0; labInd < this.labels.size(); labInd++){
		  int currCount =0;
		  String curLab = this.labels.get(labInd);
		  for(int exInd =0; exInd < examples.size(); exInd ++){
			  if(examples.get(exInd).label.equals(curLab)){
				  currCount++;
			  }
		  }
		  if(currCount > majorityCount){
			  majorityCount = currCount;
			  majorityLab = labels.get(labInd);
		  }
	  }
	  
	  return majorityLab;	  
  }
  /**
   * Checks if all of the examples have the same label
   * @param examples
   * @return
   */
  private boolean AllExamplesHaveSameLabel(List<Instance> examples){
	  //If there is 1 or less examples they all have the same label.
	  if(examples.size() <= 1){
		  return true;
	  }
	  
	  //check if the rest of the examples have the same label
	  boolean AllSame = true;
	  for (int ex = 0; ex < examples.size() - 1; ex ++){
		  AllSame = AllSame && (examples.get(ex).label.equals(examples.get(ex+1).label));
	  }
	  return AllSame;
  }
  
  
  
  /**
   * Gets the number classified correctly
   * @param tuning
   * @return
   */
  private int GetNumClassifiedCorrectly(List<Instance> tuning){
	  int correct = 0;
	  for(int tInd = 0; tInd < tuning.size(); tInd++){
		  if(classify(tuning.get(tInd)).equalsIgnoreCase(tuning.get(tInd).label)){
			  correct++;
		  }
	  }
	  return correct;
  }
  
  private void Prune(List<Instance> tuningExamples, List<Instance> trainExamples){
	  if(!root.terminal){
		  //get max depth
		  int curDepth = 0;//0;//GetMaxDepth();
		  //curDepth--;
		  while(curDepth <= GetMaxDepth()){
			  Prune(curDepth, tuningExamples, trainExamples);
			  curDepth++;
		  }
	  }
  }
  
  
  private void Prune(int pruneDepth, List<Instance> tuningSet, List<Instance> trainingSet){
	  Prune(root, tuningSet, 0, pruneDepth, trainingSet);
  }
  //TODO DER you messed up again.
  private void Prune(DecTreeNode curNode, List<Instance> tuning, int curDepth, int pruneDepth, List<Instance> currentExamples){
	  //We're at the correct depth. Now check if the node is lready terminal
	  if( curDepth == pruneDepth){
		  if(!curNode.terminal){
			int originalCorrect = GetNumClassifiedCorrectly(tuning);
			curNode.label = GetMajorityClass(currentExamples);
			curNode.terminal = true;
			if(GetNumClassifiedCorrectly(tuning) < originalCorrect){
				curNode.terminal = false;
				curNode.label = null;
			}
		  }
	  }
	  else{
		  if(!curNode.terminal){
		  for(int childInd = 0; childInd < curNode.children.size(); childInd++){
			  //Weed out the children that don't have the correct attribute value for the current node's child we are checking.
			  int attributeInd = getAttributeIndex(curNode.attribute);
			  
			  List<Instance> childExamples = GetExamplesWithSpecificAttirbute(currentExamples, attributeInd, this.attributeValues.get(this.attributes.get(attributeInd)).get(childInd));
			  
			  Prune(curNode.children.get(childInd),tuning, curDepth + 1, pruneDepth, childExamples);
		  }
		  }
	  }
  }
  

  private int GetMaxDepth(){
	  return GetDepth(root);
  }
  private int GetDepth(DecTreeNode startNode){
	  if(startNode.terminal){
		  return 0;
	  }
	  int depth =0;
	  for(int childInd = 0; childInd < startNode.children.size(); childInd ++){
		  depth = Math.max(depth, GetDepth(startNode.children.get(childInd)) + 1);
	  }
	  return depth;	  
  }
  
  /**
   * Build a decision tree given a training set then prune it using a tuning set.
   * 
   * @param train: the training set
   * @param tune: the tuning set
   */
  DecisionTreeImpl(DataSet train, DataSet tune) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: add code here
    if(this.labels.size() == 0){
    	this.root = null;
    }
    else{
    	this.root = BuildTree(train.instances, this.attributes, this.labels, new DecTreeNode(GetMajorityClass(train.instances), null, null, true), null);
    }
    
    //Pruning time. I'm a bonsai master so get ready for some great pruning. It's pruning time.
    Prune(tune.instances, train.instances);
  }

  @Override
  public String classify(Instance instance) {
	DecTreeNode temp = this.root;
	while(!temp.terminal){
		temp = temp.children.get( attributeValues.get(temp.attribute).indexOf(instance.attributes.get(getAttributeIndex(temp.attribute))));
		
	}
	
	return temp.label;
  }

  @Override
  public void rootInfoGain(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: add code here
    for(int attrInd = 0; attrInd < this.attributes.size(); attrInd++){
    	System.out.format("%s %.5f\n", this.attributes.get(attrInd),CalcInformationGain(train.instances, this.labels, this.attributes.get(attrInd)));
    } 
  }
  
  @Override
  /**
   * Print the decision tree in the specified format
   */
  public void print() {

    printTreeNode(root, null, 0);
  }

  /**
   * Prints the subtree of the node with each line prefixed by 4 * k spaces.
   */
  public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < k; i++) {
      sb.append("    ");
    }
    String value;
    if (parent == null) {
      value = "ROOT";
    } else {
      int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
      value = attributeValues.get(parent.attribute).get(attributeValueIndex);
    }
    sb.append(value);
    if (p.terminal) {
      sb.append(" (" + p.label + ")");
      System.out.println(sb.toString());
    } else {
      sb.append(" {" + p.attribute + "?}");
      System.out.println(sb.toString());
      for (DecTreeNode child : p.children) {
        printTreeNode(child, p, k + 1);
      }
    }
  }

  /**
   * Helper function to get the index of the label in labels list
   */
  private int getLabelIndex(String label) {
    for (int i = 0; i < this.labels.size(); i++) {
      if (label.equals(this.labels.get(i))) {
        return i;
      }
    }
    return -1;
  }
 
  /**
   * Helper function to get the index of the attribute in attributes list
   */
  private int getAttributeIndex(String attr) {
    for (int i = 0; i < this.attributes.size(); i++) {
      if (attr.equals(this.attributes.get(i))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper function to get the index of the attributeValue in the list for the attribute key in the attributeValues map
   */
  private int getAttributeValueIndex(String attr, String value) {
    for (int i = 0; i < attributeValues.get(attr).size(); i++) {
      if (value.equals(attributeValues.get(attr).get(i))) {
        return i;
      }
    }
    return -1;
  }
}

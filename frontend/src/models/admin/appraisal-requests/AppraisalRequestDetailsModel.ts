import AttributeModel from "../../AttributeModel";
import EmployeeDetails from "../../EmployeeDetailsModel";
import TaskModel from "../../TaskModel";

export default interface AppraisalRequestDetailsModel {
  employeeDetails: EmployeeDetails;
  attributes: AttributeModel[];
  tasks: TaskModel[];
  fullyRated:boolean;
}

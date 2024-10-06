import AttributeModel from "../../AttributeModel";
import EmployeeDetails from "../../EmployeeDetailsModel";
import TaskModel from "../../TaskModel";

export default interface AppraisalRequestDetails {
  employeeDetails: EmployeeDetails;
  attributes: AttributeModel[];
  tasks: TaskModel[];
  fullyRated:boolean;
}

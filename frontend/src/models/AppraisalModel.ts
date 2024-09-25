import AttributeModel from "./AttributeModel";
import TaskModel from "./TaskModel";

interface AppraisalModel{
    appraisalId:number;
    startDate:Date;
    endDate:Date;
    attributes:AttributeModel[];
    tasks:TaskModel[];
    status:string;
    totalRating:number;
}

export default AppraisalModel;
interface TaskModel {
  taskId?: number;
  taskTitle: string;
  description: string;
  startDate: string;
  endDate: string;
  appraisable?: boolean;
  selfRating?: number;
  adminRating?: number;
  appraisalId?: number;
  editable?:boolean;
}

export default TaskModel;

public class CarIncidentCase extends IncidentCase {
  private CarPolicySummary policy;
  private CarIncidentCategoryEnum category;
  private boolean recoveryRequired;
  private Location recoveryLocation;
  private boolean seriousInjury;
  private String crimeReference?; // optional
  private List<EmergencyServiceEnum> emergencyServices;
  private CollisionCauseEnum collisionCause;
  private CollisionTypeEnum collisionType;
  private CollisionObjectEnum collisionObjectType?; // optional
  private JourneyReasonEnum journeyReason;
}

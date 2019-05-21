namespace BookingPlaceAPI.ViewModels
{
    public class RateVM
    {
        public int? UserId { get; set; }
        public int? OrganizationId { get; set; }
        public double? Rate { get; set; }
        public string Text { get; set; }
    }
}

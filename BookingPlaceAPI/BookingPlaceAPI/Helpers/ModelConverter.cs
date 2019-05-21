using BookingPlaceAPI.Entities;
using BookingPlaceAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace BookingPlaceAPI.Helpers
{
    public class ModelConverter
    {
        public static OrganizationBookingDetail GetOrganizationBookingDetail(OrganizationEntity organization, List<OrganizationBookingPlaceEntity> bookingDetail)
        {
            if (bookingDetail == null || !bookingDetail.Any())
                return new OrganizationBookingDetail
                {
                    BookingPlaceInfo = new List<OrganizationBookingInfoByDay>
                    {
                        new OrganizationBookingInfoByDay
                        {
                            From = organization.GetWorkingStart(),
                            To = organization.GetWorkingEnd(),
                            NumberOfBuzyPlacement = 0,
                            NumberOfFreePlacement = organization.CountOfAvailablePlacement ?? 15
                        }
                    },
                    StartTime = organization.GetWorkingStart(),
                    EndTime = organization.GetWorkingEnd(),
                    SummaryNumberOfPlace = organization.CountOfAvailablePlacement ?? 15
                };

            var timePoints = new List<TimeSpan> { organization.GetWorkingStart(), organization.GetWorkingEnd() };
            bookingDetail.ForEach(bookingInfo =>
            {
                timePoints.Add(bookingInfo.GetBookingStart());
                timePoints.Add(bookingInfo.GetBookingEnd());
            });
            timePoints = timePoints.Distinct().ToList();
            timePoints.Sort();

            var bookingPlaceInfoByDay = new List<OrganizationBookingInfoByDay>();

            for (int index = 0; index < timePoints.Count - 1; index++)
            {
                var from = timePoints[index];
                var to = timePoints[index + 1];

                var numberOfBuzyPlacement = bookingDetail
                    .Where(el => el.GetBookingStart() <= from && el.GetBookingEnd() >= to)
                    .Sum(el => el.NumberOfTable);

                bookingPlaceInfoByDay.Add(new OrganizationBookingInfoByDay
                {
                    From = from,
                    To = to,
                    NumberOfBuzyPlacement = numberOfBuzyPlacement,
                    NumberOfFreePlacement = (organization.CountOfAvailablePlacement ?? 15) - numberOfBuzyPlacement
                });
            }

            return new OrganizationBookingDetail
            {
                BookingPlaceInfo = bookingPlaceInfoByDay,
                StartTime = organization.GetWorkingStart(),
                EndTime = organization.GetWorkingEnd(),
                SummaryNumberOfPlace = organization.CountOfAvailablePlacement ?? 15
            };
        }
    }
}

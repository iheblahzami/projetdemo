import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TutorialDetailsComponent } from './tutorial-details.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

describe('TutorialDetailsComponent', () => {
  let component: TutorialDetailsComponent;
  let fixture: ComponentFixture<TutorialDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TutorialDetailsComponent],
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }), // Mock params with an observable
            snapshot: { params: { id: '1' } } // Add this line to mock snapshot
          }
        }
      ]
    });
    fixture = TestBed.createComponent(TutorialDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
